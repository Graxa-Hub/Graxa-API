package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.AlocacaoEntity;
import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Enums.StatusAlocacao;
import com.Graxa_API.Graxa_API.Repository.AlocacaoRepository;
import com.Graxa_API.Graxa_API.Repository.ShowRepository;
import com.Graxa_API.Graxa_API.Repository.ColaboradorRepository;
import com.Graxa_API.Graxa_API.Security.SecurityUtils;
import com.Graxa_API.Graxa_API.dto.AlocacaoDto.RequestAlocacaoDto;
import com.Graxa_API.Graxa_API.dto.AlocacaoDto.ResponseAlocacaoDto;
import jakarta.persistence.EntityNotFoundException;
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
    private final SecurityUtils securityUtils;  // ← novo

    public AlocacaoService(AlocacaoRepository alocacaoRepository,
                           ShowRepository showRepository,
                           ColaboradorRepository colaboradorRepository,
                           NotificacaoService notificacaoService,
                           SecurityUtils securityUtils) {  // ← novo
        this.alocacaoRepository = alocacaoRepository;
        this.showRepository = showRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.notificacaoService = notificacaoService;
        this.securityUtils = securityUtils;  // ← novo
    }

    @Transactional
    public ResponseAlocacaoDto criarAlocacao(RequestAlocacaoDto dto) {
        ColaboradorEntity usuarioLogado = securityUtils.getUsuarioLogado();

        ShowEntity show = showRepository.findById(dto.showId())
                .orElseThrow(() -> new EntityNotFoundException("Show não encontrado"));

        // Verifica se o usuário logado é o criador do show
        if (!show.getCriadoPor().getId().equals(usuarioLogado.getId())) {
            throw new RuntimeException("Apenas o criador do show pode criar alocações");
        }

        ColaboradorEntity colaborador = colaboradorRepository.findById(dto.colaboradorId())
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado"));

        AlocacaoEntity alocacao = new AlocacaoEntity();
        alocacao.setShow(show);
        alocacao.setColaborador(colaborador);
        alocacao.setStatus(StatusAlocacao.PENDENTE);
        alocacao.setAtivo(true);
        alocacao.setDataHoraCriacao(LocalDateTime.now());

        AlocacaoEntity salvo = alocacaoRepository.save(alocacao);

        notificacaoService.criarNotificacao(
                colaborador.getId(),
                "Você foi alocado para o show " + show.getNomeEvento(),
                "ALOCACAO_SHOW",
                salvo.getId()
        );

        return ResponseAlocacaoDto.toResponse(salvo);
    }

    @Transactional
    public ResponseAlocacaoDto responderAlocacao(Long alocacaoId, StatusAlocacao status) {
        ColaboradorEntity usuarioLogado = securityUtils.getUsuarioLogado();

        AlocacaoEntity alocacao = alocacaoRepository.findById(alocacaoId)
                .orElseThrow(() -> new EntityNotFoundException("Alocação não encontrada"));

        // Verifica se é o destinatário da alocação
        if (!alocacao.getColaborador().getId().equals(usuarioLogado.getId())) {
            throw new RuntimeException("Apenas o colaborador alocado pode responder esta alocação");
        }

        alocacao.setStatus(status);
        alocacao.setDataHoraResposta(LocalDateTime.now());

        return ResponseAlocacaoDto.toResponse(alocacaoRepository.save(alocacao));
    }

    public List<ResponseAlocacaoDto> listarPorShow(Long showId) {
        ColaboradorEntity usuarioLogado = securityUtils.getUsuarioLogado();

        ShowEntity show = showRepository.findById(showId)
                .orElseThrow(() -> new EntityNotFoundException("Show não encontrado"));

        // Criador vê todas, outros veem só as suas
        boolean ehCriador = show.getCriadoPor().getId().equals(usuarioLogado.getId());

        List<AlocacaoEntity> alocacoes = ehCriador
                ? alocacaoRepository.findByShowId(showId)
                : alocacaoRepository.findByShowIdAndColaboradorId(showId, usuarioLogado.getId());

        return ResponseAlocacaoDto.toResponse(alocacoes);
    }
}