package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Exception.DuplicationException;
import com.Graxa_API.Graxa_API.Factory.EventoFactory;
import com.Graxa_API.Graxa_API.Repository.*;
import com.Graxa_API.Graxa_API.Security.SecurityUtils;
import com.Graxa_API.Graxa_API.dto.ShowDto.RequestBandasShowDto;
import com.Graxa_API.Graxa_API.dto.ShowDto.RequestShowDto;
import com.Graxa_API.Graxa_API.dto.ShowDto.ResponseShowDto;
import com.Graxa_API.Graxa_API.Enums.StatusAlocacao;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowService {

    private final ShowRepository repository;
    private final BandaRepository bandaRepository;
    private final TurneRepository turneRepository;
    private final LocalRepository localRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final SecurityUtils securityUtils;
    private final EventoFactory eventoFactory;

    public ShowService(
            ShowRepository repository,
            BandaRepository bandaRepository,
            TurneRepository turneRepository,
            LocalRepository localRepository,
            ColaboradorRepository colaboradorRepository,
            SecurityUtils securityUtils,
            EventoFactory eventoFactory
    ) {
        this.repository = repository;
        this.bandaRepository = bandaRepository;
        this.turneRepository = turneRepository;
        this.localRepository = localRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.securityUtils = securityUtils;
        this.eventoFactory = eventoFactory;
    }

    // Produtor criador OU colaborador com alocação CONFIRMADA
    private boolean temAcesso(ShowEntity show, ColaboradorEntity logado) {
        boolean ehCriador = show.getCriadoPor().getId().equals(logado.getId());
        boolean estaAlocado = show.getAlocacoes().stream()
                .anyMatch(a -> a.getColaborador().getId().equals(logado.getId())
                        && a.getStatus() == StatusAlocacao.ACEITO);
        return ehCriador || estaAlocado;
    }

    @Transactional
    public ResponseEntity<ResponseShowDto> criar(RequestShowDto dto) {
        if (repository.existsByNomeEvento(dto.nomeEvento())) {
            throw new DuplicationException(dto.nomeEvento());
        }
        ShowEntity show = (ShowEntity) eventoFactory.criarEvento(dto);
        show.setCriadoPor(securityUtils.getUsuarioLogado());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseShowDto.toResponse(repository.save(show)));
    }

    public ResponseEntity<ResponseShowDto> buscarPorId(Long id) {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();
        ShowEntity show = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Show não encontrado"));

        if (!temAcesso(show, logado)) {
            throw new RuntimeException("Acesso negado");
        }

        return ResponseEntity.ok(ResponseShowDto.toResponse(show));
    }

    public ResponseEntity<List<ResponseShowDto>> listarTodos() {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();

        List<ShowEntity> shows = repository.findByAtivoTrue().stream()
                .filter(show -> temAcesso(show, logado))
                .toList();

        return shows.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(ResponseShowDto.toResponse(shows));
    }

    @Transactional
    public ResponseEntity<ResponseShowDto> atualizar(Long id, RequestShowDto dto) {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();
        ShowEntity show = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Show não encontrado"));

        // Só o criador pode atualizar
        if (!show.getCriadoPor().getId().equals(logado.getId())) {
            throw new RuntimeException("Acesso negado");
        }

        if (dto.nomeEvento() != null) show.setNomeEvento(dto.nomeEvento());
        if (dto.dataInicio() != null) show.setDataInicio(dto.dataInicio());
        if (dto.dataFim() != null) show.setDataFim(dto.dataFim());
        if (dto.descricao() != null) show.setDescricao(dto.descricao());
        if (dto.turneId() != null) show.setTurne(turneRepository.findById(dto.turneId())
                .orElseThrow(() -> new EntityNotFoundException("Turnê não encontrada")));
        if (dto.localId() != null) show.setLocal(localRepository.findById(dto.localId())
                .orElseThrow(() -> new EntityNotFoundException("Local não encontrado")));
        if (dto.responsavelId() != null) show.setResponsavelEvento(colaboradorRepository.findById(dto.responsavelId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado")));

        return ResponseEntity.ok(ResponseShowDto.toResponse(repository.save(show)));
    }

    @Transactional
    public ResponseEntity<Void> deletar(Long id) {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();
        ShowEntity show = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Show não encontrado"));

        // Só o criador pode deletar
        if (!show.getCriadoPor().getId().equals(logado.getId())) {
            throw new RuntimeException("Acesso negado");
        }

        show.setAtivo(false);
        repository.save(show);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<ResponseShowDto> adicionarBandasAoShow(RequestBandasShowDto dto) {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();
        ShowEntity show = repository.findById(dto.showId())
                .orElseThrow(() -> new EntityNotFoundException("Show não encontrado"));

        // Só o criador pode adicionar bandas
        if (!show.getCriadoPor().getId().equals(logado.getId())) {
            throw new RuntimeException("Acesso negado");
        }

        List<BandaEntity> bandas = bandaRepository.findAllById(dto.bandasIds());
        if (bandas.isEmpty()) throw new EntityNotFoundException("Nenhuma banda encontrada");

        show.setBandas(bandas);
        return ResponseEntity.ok(ResponseShowDto.toResponse(repository.save(show)));
    }
}