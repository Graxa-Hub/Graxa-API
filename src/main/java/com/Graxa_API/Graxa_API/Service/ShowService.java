package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.Exception.DuplicationException;
import com.Graxa_API.Graxa_API.Factory.EventoFactory;
import com.Graxa_API.Graxa_API.Repository.*;
import com.Graxa_API.Graxa_API.dto.ShowDto.RequestBandasShowDto;
import com.Graxa_API.Graxa_API.dto.ShowDto.RequestShowDto;
import com.Graxa_API.Graxa_API.dto.ShowDto.ResponseShowDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowService {

    private final ShowRepository repository;
    private final BandaRepository bandaRepository;
    private final TurneRepository turneRepository;
    private final LocalRepository localRepository;
    private final UsuarioRepository usuarioRepository;
    private final EventoFactory eventoFactory;

    public ShowService(
            ShowRepository repository,
            BandaRepository bandaRepository,
            TurneRepository turneRepository,
            LocalRepository localRepository,
            UsuarioRepository usuarioRepository,
            EventoFactory eventoFactory
    ) {
        this.repository = repository;
        this.bandaRepository = bandaRepository;
        this.turneRepository = turneRepository;
        this.localRepository = localRepository;
        this.usuarioRepository = usuarioRepository;
        this.eventoFactory = eventoFactory;
    }

    @Transactional
    public ResponseEntity<ResponseShowDto> criar(RequestShowDto dto) {
        if (repository.existsByNomeEvento(dto.nomeEvento())) {
            throw new DuplicationException(dto.nomeEvento());
        }

        ShowEntity show = (ShowEntity) eventoFactory.criarEvento(dto);

        ShowEntity salvo = repository.save(show);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseShowDto.toResponse(salvo));
    }

    public ResponseEntity<ResponseShowDto> buscarPorId(Long id) {
        ShowEntity show = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Show não encontrado"));
        return ResponseEntity.ok(ResponseShowDto.toResponse(show));
    }

    public ResponseEntity<List<ResponseShowDto>> listarTodos() {
        List<ShowEntity> shows = repository.findByAtivoTrue();
        return shows.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(ResponseShowDto.toResponse(shows));
    }

    @Transactional
    public ResponseEntity<ResponseShowDto> atualizar(Long id, RequestShowDto dto) {
        ShowEntity show = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Show não encontrado"));

        if (dto.nomeEvento() != null) {
            show.setNomeEvento(dto.nomeEvento());
        }
        if (dto.dataInicio() != null) {
            show.setDataInicio(dto.dataInicio());
        }
        if (dto.dataFim() != null) {
            show.setDataFim(dto.dataFim());
        }
        if (dto.descricao() != null) {
            show.setDescricao(dto.descricao());
        }
        if (dto.turneId() != null) {
            show.setTurne(turneRepository.findById(dto.turneId())
                    .orElseThrow(() -> new EntityNotFoundException("Turnê não encontrada")));
        }
        if (dto.localId() != null) {
            show.setLocal(localRepository.findById(dto.localId())
                    .orElseThrow(() -> new EntityNotFoundException("Local não encontrado")));
        }
        if (dto.responsavelId() != null) {
            show.setResponsavelEvento(usuarioRepository.findById(dto.responsavelId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado")));
        }

        ShowEntity atualizado = repository.save(show);
        return ResponseEntity.ok(ResponseShowDto.toResponse(atualizado));
    }

    @Transactional
    public ResponseEntity<Void> deletar(Long id) {
        ShowEntity show = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Show não encontrado"));
        show.setAtivo(false);
        repository.save(show);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<ResponseShowDto> adicionarBandasAoShow(RequestBandasShowDto dto) {
        ShowEntity show = repository.findById(dto.showId())
                .orElseThrow(() -> new EntityNotFoundException("Show não encontrado"));

        List<BandaEntity> bandas = bandaRepository.findAllById(dto.bandasIds());
        if (bandas.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma banda encontrada com os IDs fornecidos");
        }

        show.setBandas(bandas);
        ShowEntity atualizado = repository.save(show);
        return ResponseEntity.ok(ResponseShowDto.toResponse(atualizado));
    }
}
