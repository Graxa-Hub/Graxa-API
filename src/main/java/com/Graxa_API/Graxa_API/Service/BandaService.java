package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Exception.BandaDuplicadaException;
import com.Graxa_API.Graxa_API.Exception.BandaNaoEncontradaException;
import com.Graxa_API.Graxa_API.Exception.BandasNaoEncontradasException;
import com.Graxa_API.Graxa_API.Exception.UsuarioNaoEncontradoException;
import com.Graxa_API.Graxa_API.Repository.BandaRepository;
import com.Graxa_API.Graxa_API.Repository.UsuarioRepository;
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
    private final UsuarioRepository usuarioRepository;

    public BandaService(BandaRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }


    public ResponseEntity<List<ResponseBandaDto>> getBandas(){
        List<BandaEntity> bandas = repository.findAll();
        if(bandas.isEmpty()){
            throw new BandasNaoEncontradasException();
        }
        return ResponseEntity.ok(ResponseBandaDto.toResponse(bandas));
    }

    public ResponseEntity<ResponseBandaDto> getBandaPorId(Long id){
        BandaEntity banda = repository.findById(id).orElseThrow(()-> new BandaNaoEncontradaException(id));
        return ResponseEntity.ok(ResponseBandaDto.toResponse(banda));
    }

    @Transactional
    public ResponseEntity<ResponseBandaDto> criarBanda(RequestBandaDto banda){
        if(repository.existsByNome(banda.nome())){
            throw new BandaDuplicadaException(banda.nome());
        }

        BandaEntity saved = repository.save(new BandaEntity(banda));

        return ResponseEntity.status(201).body(ResponseBandaDto.toResponse(saved));
    }

    public ResponseEntity<ResponseBandaDto> adicionarIntegranteBanda(Long bandaId, RequestIntegrantesDto dto) {
        BandaEntity banda = repository.findById(bandaId)
                .orElseThrow(() -> new BandaNaoEncontradaException(bandaId));

        List<ColaboradorEntity> integrantes = dto.integrantesIds().stream()
                .map(id -> usuarioRepository.findById(id)
                        .orElseThrow(() -> new UsuarioNaoEncontradoException(id)))
                .toList();

        banda.getIntegrantes().addAll(integrantes);
        repository.save(banda);

        return ResponseEntity.ok(ResponseBandaDto.toResponse(banda));
    }

}
