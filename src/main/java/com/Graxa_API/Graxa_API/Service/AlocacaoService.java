package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.AlocacaoEntity;
import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Enums.StatusAlocacao;
import com.Graxa_API.Graxa_API.Repository.AlocacaoRepository;
import com.Graxa_API.Graxa_API.Repository.ShowRepository;
import com.Graxa_API.Graxa_API.Repository.ColaboradorRepository;
import com.Graxa_API.Graxa_API.messaging.EmailAlocacaoPublisher;
import com.Graxa_API.Graxa_API.dto.AlocacaoDto.RequestAlocacaoDto;
import com.Graxa_API.Graxa_API.dto.AlocacaoDto.ResponseAlocacaoDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlocacaoService {
    private final AlocacaoRepository alocacaoRepository;
    private final ShowRepository showRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final NotificacaoService notificacaoService;
    private final EmailAlocacaoPublisher emailAlocacaoPublisher;

    public AlocacaoService(AlocacaoRepository alocacaoRepository,
                           ShowRepository showRepository,
                           ColaboradorRepository colaboradorRepository,
                           NotificacaoService notificacaoService,
                           EmailAlocacaoPublisher emailAlocacaoPublisher) {
        this.alocacaoRepository = alocacaoRepository;
        this.showRepository = showRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.notificacaoService = notificacaoService;
        this.emailAlocacaoPublisher = emailAlocacaoPublisher;
    }

    @Transactional
    public ResponseAlocacaoDto criarAlocacao(RequestAlocacaoDto dto) {
        ShowEntity show = showRepository.findById(dto.showId())
                .orElseThrow(() -> new RuntimeException("Show não encontrado"));
        ColaboradorEntity colaborador = colaboradorRepository.findById(dto.colaboradorId())
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));

        AlocacaoEntity alocacao = new AlocacaoEntity();
        alocacao.setShow(show);
        alocacao.setColaborador(colaborador);
        alocacao.setStatus(StatusAlocacao.PENDENTE);
        alocacao.setAtivo(true);
        alocacao.setDataHoraCriacao(LocalDateTime.now());

        AlocacaoEntity salvo = alocacaoRepository.save(alocacao);

        // Dispara notificação
        notificacaoService.criarNotificacao(
                colaborador.getId(),
                "Você foi alocado para o show " + show.getNomeEvento(),
                "ALOCACAO_SHOW",
                salvo.getId()
        );
        emailAlocacaoPublisher.publicarEmailDeAlocacao(colaborador, show);

        return ResponseAlocacaoDto.toResponse(salvo);
    }

    @Transactional
    public ResponseAlocacaoDto responderAlocacao(Long alocacaoId, StatusAlocacao status) {
        AlocacaoEntity alocacao = alocacaoRepository.findById(alocacaoId)
                .orElseThrow(() -> new RuntimeException("Alocação não encontrada"));

        alocacao.setStatus(status);
        alocacao.setDataHoraResposta(LocalDateTime.now());

        AlocacaoEntity atualizado = alocacaoRepository.save(alocacao);
        return ResponseAlocacaoDto.toResponse(atualizado);
    }

    public List<ResponseAlocacaoDto> listarPorShow(Long showId) {
        List<AlocacaoEntity> alocacoes = alocacaoRepository.findByShowId(showId);
        return ResponseAlocacaoDto.toResponse(alocacoes);
    }
}
