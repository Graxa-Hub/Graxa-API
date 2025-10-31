package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ArtistaEntity;
import com.Graxa_API.Graxa_API.Exception.BandaDuplicadaException;
import com.Graxa_API.Graxa_API.Exception.BandaNaoEncontradaException;
import com.Graxa_API.Graxa_API.Exception.BandasNaoEncontradasException;
import com.Graxa_API.Graxa_API.Exception.UsuarioNaoEncontradoException;
import com.Graxa_API.Graxa_API.Repository.ArtistaRepository;
import com.Graxa_API.Graxa_API.Repository.BandaRepository;
import com.Graxa_API.Graxa_API.dto.BandaDto.RequestBandaDto;
import com.Graxa_API.Graxa_API.dto.BandaDto.RequestIntegrantesDto;
import com.Graxa_API.Graxa_API.dto.BandaDto.ResponseBandaDto;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BandaService {
    private final BandaRepository repository;
    private final ArtistaRepository artistaRepository;

    public BandaService(BandaRepository repository, ArtistaRepository artistaRepository) {
        this.repository = repository;
        this.artistaRepository = artistaRepository;
    }

    public ResponseEntity<List<ResponseBandaDto>> getBandas() {
        List<BandaEntity> bandas = repository.findAll();
        if (bandas.isEmpty()) {
            throw new BandasNaoEncontradasException();
        }
        return ResponseEntity.ok(ResponseBandaDto.toResponse(bandas));
    }

    public ResponseEntity<ResponseBandaDto> getBandaPorId(Long id) {
        BandaEntity banda = repository.findById(id)
                .orElseThrow(() -> new BandaNaoEncontradaException(id));
        return ResponseEntity.ok(ResponseBandaDto.toResponse(banda));
    }

    @Transactional
    public ResponseEntity<ResponseBandaDto> criarBanda(RequestBandaDto dto) {
        if (repository.existsByNome(dto.nome())) {
            throw new BandaDuplicadaException(dto.nome());
        }

        BandaEntity banda = new BandaEntity();
        banda.setNome(dto.nome());
        banda.setDescricao(dto.descricao());
        banda.setGenero(dto.genero());

        BandaEntity saved = repository.save(banda);
        return ResponseEntity.status(201).body(ResponseBandaDto.toResponse(saved));
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
}
