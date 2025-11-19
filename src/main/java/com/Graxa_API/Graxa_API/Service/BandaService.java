package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import com.Graxa_API.Graxa_API.Entity.ImagemEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ArtistaEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.RepresentanteEntity;
import com.Graxa_API.Graxa_API.Exception.BandaDuplicadaException;
import com.Graxa_API.Graxa_API.Exception.BandaNaoEncontradaException;
import com.Graxa_API.Graxa_API.Exception.BandasNaoEncontradasException;
import com.Graxa_API.Graxa_API.Exception.UsuarioNaoEncontradoException;
import com.Graxa_API.Graxa_API.Repository.ArtistaRepository;
import com.Graxa_API.Graxa_API.Repository.BandaRepository;
import com.Graxa_API.Graxa_API.Repository.RepresentanteRepository;
import com.Graxa_API.Graxa_API.dto.BandaDto.RequestBandaDto;
import com.Graxa_API.Graxa_API.dto.BandaDto.RequestIntegrantesDto;
import com.Graxa_API.Graxa_API.dto.BandaDto.ResponseBandaDto;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BandaService {
    private final BandaRepository repository;
    private final ArtistaRepository artistaRepository;
    private final RepresentanteRepository representanteRepository;
    private final ImagemService imagemService;

    public BandaService(
            BandaRepository repository,
            ArtistaRepository artistaRepository,
            RepresentanteRepository representanteRepository,
            ImagemService imagemService
    ) {
        this.repository = repository;
        this.artistaRepository = artistaRepository;
        this.representanteRepository = representanteRepository;
        this.imagemService = imagemService;
    }

    public ResponseEntity<List<ResponseBandaDto>> getBandas() {
        Optional<List<BandaEntity>> bandas = repository.findByAtivoTrue();
        if (bandas.get().isEmpty()) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok(ResponseBandaDto.toResponse(bandas.get()));
    }

    public ResponseEntity<ResponseBandaDto> getBandaPorId(Long id) {
        BandaEntity banda = repository.findById(id)
                .orElseThrow(() -> new BandaNaoEncontradaException(id));
        return ResponseEntity.ok(ResponseBandaDto.toResponse(banda));
    }

    @Transactional
    public ResponseEntity<ResponseBandaDto> criarBanda(RequestBandaDto dto, MultipartFile foto) throws IOException {
        if (repository.existsByNome(dto.nome())) {
            throw new BandaDuplicadaException(dto.nome());
        }

        // Busca o representante
        RepresentanteEntity representante = representanteRepository.findById(dto.representanteId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(dto.representanteId()));

        BandaEntity banda = new BandaEntity();
        banda.setNome(dto.nome());
        banda.setDescricao(dto.descricao());
        banda.setGenero(dto.genero());
        banda.setRepresentante(representante);

        // Salva a imagem se foi enviada
        if (foto != null && !foto.isEmpty()) {
            ResponseEntity<ImagemEntity> imagemResponse = imagemService.salvarImagem(foto);
            ImagemEntity imagemSalva = imagemResponse.getBody();

            if (imagemSalva != null) {
                banda.setNomeFoto(imagemSalva.getNomeArquivo());
            }
        }

        BandaEntity saved = repository.save(banda);
        return ResponseEntity.status(201).body(ResponseBandaDto.toResponse(saved));
    }

    @Transactional
    public ResponseEntity<ResponseBandaDto> atualizarBanda(Long id, RequestBandaDto dto, MultipartFile foto) throws IOException {
        BandaEntity banda = repository.findById(id)
                .orElseThrow(() -> new BandaNaoEncontradaException(id));

        banda.setNome(dto.nome());
        banda.setDescricao(dto.descricao());
        banda.setGenero(dto.genero());

        // Atualiza representante se mudou
        if (!banda.getRepresentante().getId().equals(dto.representanteId())) {
            RepresentanteEntity novoRepresentante = representanteRepository.findById(dto.representanteId())
                    .orElseThrow(() -> new UsuarioNaoEncontradoException(dto.representanteId()));
            banda.setRepresentante(novoRepresentante);
        }

        // Atualiza a imagem se foi enviada nova
        if (foto != null && !foto.isEmpty()) {
            ResponseEntity<ImagemEntity> imagemResponse = imagemService.salvarImagem(foto);
            ImagemEntity imagemSalva = imagemResponse.getBody();

            if (imagemSalva != null) {
                banda.setNomeFoto(imagemSalva.getNomeArquivo());
            }
        }

        BandaEntity atualizada = repository.save(banda);
        return ResponseEntity.ok(ResponseBandaDto.toResponse(atualizada));
    }

    @Transactional
    public ResponseEntity<ResponseBandaDto> adicionarIntegranteBanda(Long bandaId, RequestIntegrantesDto dto) {
        BandaEntity banda = repository.findById(bandaId)
                .orElseThrow(() -> new BandaNaoEncontradaException(bandaId));

        List<ArtistaEntity> integrantes = dto.integrantesIds().stream()
                .map(id -> artistaRepository.findById(id)
                        .orElseThrow(() -> new UsuarioNaoEncontradoException(id)))
                .toList();

        banda.getIntegrantes().addAll(integrantes);
        repository.save(banda);

        return ResponseEntity.ok(ResponseBandaDto.toResponse(banda));
    }

    @Transactional
    public void deletarBanda(Long bandaId) {
        ResponseEntity<ResponseBandaDto> responseBanda = getBandaPorId(bandaId);
        if (!responseBanda.getStatusCode().isError()) {
            Optional<BandaEntity> bandaOpt = repository.findById(bandaId);
            if (bandaOpt.isPresent()) {
                BandaEntity banda = bandaOpt.get();
                banda.setAtivo(false);

                // Safe delete em todos os integrantes
                if (banda.getIntegrantes() != null) {
                    banda.getIntegrantes().forEach(integrante -> integrante.setAtivo(false));
                }

                repository.save(banda); // Salva as alterações
            }
        }
    }
}