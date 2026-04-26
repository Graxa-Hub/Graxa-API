package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.Entity.Evento.ViagemEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Enums.StatusAlocacao;
import com.Graxa_API.Graxa_API.Exception.DuplicationException;
import com.Graxa_API.Graxa_API.Factory.EventoFactory;
import com.Graxa_API.Graxa_API.Repository.ShowRepository;
import com.Graxa_API.Graxa_API.Repository.ViagemRepository;
import com.Graxa_API.Graxa_API.Security.SecurityUtils;
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
    private final EventoFactory eventoFactory;
    private final ShowRepository showRepository;
    private final SecurityUtils securityUtils;

    public ViagemService(
            ViagemRepository repository,
            EventoFactory eventoFactory,
            ShowRepository showRepository,
            SecurityUtils securityUtils
    ) {
        this.repository = repository;
        this.eventoFactory = eventoFactory;
        this.showRepository = showRepository;
        this.securityUtils = securityUtils;
    }

    private boolean temAcesso(ViagemEntity viagem, ColaboradorEntity logado) {
        if (viagem.getShow() == null || viagem.getShow().getCriadoPor() == null) return false;

        boolean ehCriador = viagem.getShow().getCriadoPor().getId().equals(logado.getId());
        boolean estaAlocado = viagem.getShow().getAlocacoes().stream()
                .anyMatch(a -> a.getColaborador().getId().equals(logado.getId())
                        && a.getStatus() == StatusAlocacao.ACEITO);
        return ehCriador || estaAlocado;
    }

    @Transactional
    public ResponseEntity<ResponseViagemDto> criar(RequestViagemDto dto) {
        if (repository.existsByNomeEvento(dto.nomeEvento())) {
            throw new DuplicationException(dto.nomeEvento());
        }

        ViagemEntity viagem = (ViagemEntity) eventoFactory.criarEvento(dto);
        viagem.setCriadoPor(securityUtils.getUsuarioLogado());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseViagemDto.toResponse(repository.save(viagem)));
    }

    public ResponseEntity<ResponseViagemDto> buscarPorId(Long id) {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();
        ViagemEntity viagem = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Viagem não encontrada"));

        if (!temAcesso(viagem, logado)) throw new RuntimeException("Acesso negado");

        return ResponseEntity.ok(ResponseViagemDto.toResponse(viagem));
    }

    public ResponseEntity<List<ResponseViagemDto>> listarTodos() {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();

        List<ViagemEntity> viagens = repository.findByAtivoTrue().stream()
                .filter(v -> temAcesso(v, logado))
                .toList();

        return viagens.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(ResponseViagemDto.toResponse(viagens));
    }

    public ResponseEntity<List<ResponseViagemDto>> buscarPorNome(String nome) {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();

        List<ViagemEntity> viagens = repository.findByNomeEventoContainingIgnoreCaseAndAtivoTrue(nome).stream()
                .filter(v -> temAcesso(v, logado))
                .toList();

        return viagens.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(ResponseViagemDto.toResponse(viagens));
    }

    @Transactional
    public ResponseEntity<ResponseViagemDto> atualizar(Long id, RequestViagemDto dto) {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();
        ViagemEntity viagem = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Viagem não encontrada"));

        if (!viagem.getCriadoPor().getId().equals(logado.getId())) {
            throw new RuntimeException("Acesso negado");
        }

        if (dto.nomeEvento() != null) viagem.setNomeEvento(dto.nomeEvento());
        if (dto.dataInicio() != null) viagem.setDataInicio(dto.dataInicio());
        if (dto.dataFim() != null) viagem.setDataFim(dto.dataFim());
        if (dto.descricao() != null) viagem.setDescricao(dto.descricao());
        if (dto.showId() != null) viagem.setShow(showRepository.findById(dto.showId())
                .orElseThrow(() -> new EntityNotFoundException("Show não encontrado")));
        if (dto.tipoViagem() != null) viagem.setTipoViagem(dto.tipoViagem());

        return ResponseEntity.ok(ResponseViagemDto.toResponse(repository.save(viagem)));
    }

    @Transactional
    public ResponseEntity<Void> deletar(Long id) {
        ColaboradorEntity logado = securityUtils.getUsuarioLogado();
        ViagemEntity viagem = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Viagem não encontrada"));

        if (!viagem.getCriadoPor().getId().equals(logado.getId())) {
            throw new RuntimeException("Acesso negado");
        }

        viagem.setAtivo(false);
        repository.save(viagem);
        return ResponseEntity.noContent().build();
    }
}