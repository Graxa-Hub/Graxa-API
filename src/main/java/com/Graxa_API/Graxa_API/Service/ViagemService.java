package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import com.Graxa_API.Graxa_API.Entity.Evento.ViagemEntity;
import com.Graxa_API.Graxa_API.Exception.DuplicationException;
import com.Graxa_API.Graxa_API.Factory.EventoFactory;
import com.Graxa_API.Graxa_API.Repository.BandaRepository;
import com.Graxa_API.Graxa_API.Repository.TurneRepository;
import com.Graxa_API.Graxa_API.Repository.ViagemRepository;
import com.Graxa_API.Graxa_API.dto.ViagemDto.RequestViagemDto;
import com.Graxa_API.Graxa_API.dto.ViagemDto.ResponseViagemDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViagemService {

    private final ViagemRepository repository;
    private final BandaRepository bandaRepository;
    private final EventoFactory eventoFactory;
    private final TurneRepository turneRepository;

    public ViagemService(
            ViagemRepository repository,
            BandaRepository bandaRepository,
            EventoFactory eventoFactory, TurneRepository turneRepository
    ) {
        this.repository = repository;
        this.bandaRepository = bandaRepository;
        this.eventoFactory = eventoFactory;
        this.turneRepository = turneRepository;
    }

    @Transactional
    public ResponseEntity<ResponseViagemDto> criar(RequestViagemDto dto) {
        if (repository.existsByNomeEvento(dto.nomeEvento())) {
            throw new DuplicationException(dto.nomeEvento());
        }

        ViagemEntity viagem = (ViagemEntity) eventoFactory.criarEvento(dto);
        ViagemEntity salvo = repository.save(viagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseViagemDto.toResponse(salvo));
    }

    public ResponseEntity<ResponseViagemDto> buscarPorId(Long id) {
        ViagemEntity viagem = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Viagem não encontrada"));
        return ResponseEntity.ok(ResponseViagemDto.toResponse(viagem));
    }

    public ResponseEntity<List<ResponseViagemDto>> listarTodos() {
        List<ViagemEntity> viagens = repository.findByAtivoTrue();
        return viagens.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(ResponseViagemDto.toResponse(viagens));
    }

    public ResponseEntity<List<ResponseViagemDto>> buscarPorNome(String nome) {
        List<ViagemEntity> viagens = repository.findByNomeEventoContainingIgnoreCaseAndAtivoTrue(nome);
        return viagens.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(ResponseViagemDto.toResponse(viagens));
    }

    @Transactional
    public ResponseEntity<ResponseViagemDto> atualizar(Long id, RequestViagemDto dto) {
        ViagemEntity viagem = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Viagem não encontrada"));

        if (dto.nomeEvento() != null) viagem.setNomeEvento(dto.nomeEvento());
        if (dto.dataInicio() != null) viagem.setDataInicio(dto.dataInicio());
        if (dto.dataFim() != null) viagem.setDataFim(dto.dataFim());
        if (dto.descricao() != null) viagem.setDescricao(dto.descricao());
        if (dto.turneId() != null) {
            viagem.setTurne(turneRepository.findById(dto.turneId())
                    .orElseThrow(() -> new EntityNotFoundException("Turnê não encontrada")));
        }
        if (dto.tipoViagem() != null) viagem.setTipoViagem(dto.tipoViagem());

        ViagemEntity atualizado = repository.save(viagem);
        return ResponseEntity.ok(ResponseViagemDto.toResponse(atualizado));
    }

    @Transactional
    public ResponseEntity<Void> deletar(Long id) {
        ViagemEntity viagem = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Viagem não encontrada"));
        viagem.setAtivo(false);
        repository.save(viagem);
        return ResponseEntity.noContent().build();
    }


}
