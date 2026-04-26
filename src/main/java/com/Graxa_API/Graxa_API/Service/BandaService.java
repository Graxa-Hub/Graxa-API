package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import com.Graxa_API.Graxa_API.Entity.ImagemEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ArtistaEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.RepresentanteEntity;
import com.Graxa_API.Graxa_API.Exception.BandaDuplicadaException;
import com.Graxa_API.Graxa_API.Exception.BandaNaoEncontradaException;
import com.Graxa_API.Graxa_API.Exception.BandasNaoEncontradasException;
import com.Graxa_API.Graxa_API.Exception.UsuarioNaoEncontradoException;
import com.Graxa_API.Graxa_API.Repository.ArtistaRepository;
import com.Graxa_API.Graxa_API.Repository.BandaRepository;
import com.Graxa_API.Graxa_API.Repository.RepresentanteRepository;
import com.Graxa_API.Graxa_API.Security.SecurityUtils;
import com.Graxa_API.Graxa_API.dto.BandaDto.RequestBandaDto;
import com.Graxa_API.Graxa_API.dto.BandaDto.RequestIntegrantesDto;
import com.Graxa_API.Graxa_API.dto.BandaDto.ResponseBandaDto;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final SecurityUtils securityUtils;  // ← novo

    public BandaService(
            BandaRepository repository,
            ArtistaRepository artistaRepository,
            RepresentanteRepository representanteRepository,
            ImagemService imagemService,
            SecurityUtils securityUtils  // ← novo
    ) {
        this.repository = repository;
        this.artistaRepository = artistaRepository;
        this.representanteRepository = representanteRepository;
        this.imagemService = imagemService;
        this.securityUtils = securityUtils;
    }

    // Produtor só vê suas próprias bandas
    public Page<ResponseBandaDto> getBandas(Pageable pageable) {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();
        Page<BandaEntity> bandas = repository.findByAtivoTrueAndCriadoPorId(logado.getId(), pageable);
        return bandas.map(ResponseBandaDto::toResponse);
    }

    public ResponseEntity<ResponseBandaDto> getBandaPorId(Long id) {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();
        BandaEntity banda = repository.findById(id)
                .orElseThrow(() -> new BandaNaoEncontradaException(id));

        if (!banda.getCriadoPor().getId().equals(logado.getId())) {
            throw new RuntimeException("Acesso negado");
        }

        return ResponseEntity.ok(ResponseBandaDto.toResponse(banda));
    }

    @Transactional
    public ResponseEntity<ResponseBandaDto> criarBanda(RequestBandaDto dto, MultipartFile foto) throws IOException {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();

        // Duplicada apenas se for do mesmo criador
        if (repository.existsByNomeAndCriadoPorId(dto.nome(), logado.getId())) {
            throw new BandaDuplicadaException(dto.nome());
        }

        RepresentanteEntity representante = representanteRepository.findById(dto.representanteId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(dto.representanteId()));

        BandaEntity banda = new BandaEntity();
        banda.setNome(dto.nome());
        banda.setDescricao(dto.descricao());
        banda.setGenero(dto.genero());
        banda.setRepresentante(representante);
        banda.setCriadoPor(logado);

        if (foto != null && !foto.isEmpty()) {
            ImagemEntity imagemSalva = imagemService.salvarImagem(foto);
            if (imagemSalva != null) banda.setNomeFoto(imagemSalva.getNomeArquivo());
        }

        return ResponseEntity.status(201).body(ResponseBandaDto.toResponse(repository.save(banda)));
    }
    @Transactional
    public ResponseEntity<ResponseBandaDto> adicionarIntegranteBanda(Long bandaId, RequestIntegrantesDto dto) {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();
        BandaEntity banda = repository.findById(bandaId)
                .orElseThrow(() -> new BandaNaoEncontradaException(bandaId));

        // Só o criador pode adicionar integrantes
        if (!banda.getCriadoPor().getId().equals(logado.getId())) {
            throw new RuntimeException("Acesso negado");
        }

        List<ArtistaEntity> integrantes = dto.integrantesIds().stream()
                .map(id -> artistaRepository.findById(id)
                        .orElseThrow(() -> new UsuarioNaoEncontradoException(id)))
                .toList();

        banda.getIntegrantes().addAll(integrantes);
        repository.save(banda);

        return ResponseEntity.ok(ResponseBandaDto.toResponse(banda));
    }

    @Transactional
    public ResponseEntity<ResponseBandaDto> atualizarBanda(Long id, RequestBandaDto dto, MultipartFile foto) throws IOException {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();
        BandaEntity banda = repository.findById(id)
                .orElseThrow(() -> new BandaNaoEncontradaException(id));

        // Só o criador pode atualizar
        if (!banda.getCriadoPor().getId().equals(logado.getId())) {
            throw new RuntimeException("Acesso negado");
        }

        banda.setNome(dto.nome());
        banda.setDescricao(dto.descricao());
        banda.setGenero(dto.genero());

        if (!banda.getRepresentante().getId().equals(dto.representanteId())) {
            RepresentanteEntity novoRepresentante = representanteRepository.findById(dto.representanteId())
                    .orElseThrow(() -> new UsuarioNaoEncontradoException(dto.representanteId()));
            banda.setRepresentante(novoRepresentante);
        }

        if (foto != null && !foto.isEmpty()) {
            ImagemEntity imagemSalva = imagemService.salvarImagem(foto);
            if (imagemSalva != null) banda.setNomeFoto(imagemSalva.getNomeArquivo());
        }

        return ResponseEntity.ok(ResponseBandaDto.toResponse(repository.save(banda)));
    }

    @Transactional
    public void deletarBanda(Long bandaId) {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();
        BandaEntity banda = repository.findById(bandaId)
                .orElseThrow(() -> new BandaNaoEncontradaException(bandaId));

        // Só o criador pode deletar
        if (!banda.getCriadoPor().getId().equals(logado.getId())) {
            throw new RuntimeException("Acesso negado");
        }

        banda.setAtivo(false);
        if (banda.getIntegrantes() != null) {
            banda.getIntegrantes().forEach(i -> i.setAtivo(false));
        }
        repository.save(banda);
    }
    public List<ResponseBandaDto> getBandasPorNome(String nome) {
        List<BandaEntity> bandas = repository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome);

        if (bandas.isEmpty()) {
            throw new BandasNaoEncontradasException();
        }

        return bandas.stream()
                .map(ResponseBandaDto::toResponse)
                .toList();
    }

}

