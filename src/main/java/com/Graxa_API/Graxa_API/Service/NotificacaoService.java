package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.AlocacaoEntity;
import com.Graxa_API.Graxa_API.Entity.NotificacaoEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Repository.AlocacaoRepository;
import com.Graxa_API.Graxa_API.Repository.ColaboradorRepository;
import com.Graxa_API.Graxa_API.Repository.NotificacaoRepository;
import com.Graxa_API.Graxa_API.dto.NotificacaoDto.RequestNotificacaoDto;
import com.Graxa_API.Graxa_API.dto.NotificacaoDto.ResponseNotificacaoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final AlocacaoRepository alocacaoRepository;

    @Autowired
    private NotificacaoWebSocketService webSocketService;

    public NotificacaoService(NotificacaoRepository notificacaoRepository,
                              ColaboradorRepository colaboradorRepository,
                              AlocacaoRepository alocacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.alocacaoRepository = alocacaoRepository;
    }

    // ✅ ATUALIZADO: Criar notificação + WebSocket
    @Transactional
    public ResponseNotificacaoDto criarNotificacao(RequestNotificacaoDto request) {
        NotificacaoEntity notificacao = criarNotificacaoEntity(
                request.colaboradorId(),
                request.mensagem(),
                request.tipo(),
                request.alocacaoId()
        );

        ResponseNotificacaoDto response = ResponseNotificacaoDto.toResponse(notificacao);

        // ✅ CORRIGIDO: Pegar email através da relação
        String username = obterUsernameDoColaborador(notificacao.getColaborador());
        if (username != null) {
            webSocketService.enviarNotificacaoParaColaborador(username, response);
        }

        return response;
    }

    // ✅ ATUALIZADO: Criar notificação de alocação + WebSocket
    @Transactional
    public ResponseNotificacaoDto criarNotificacaoAlocacao(Long alocacaoId, String mensagemPersonalizada) {
        AlocacaoEntity alocacao = alocacaoRepository.findById(alocacaoId)
                .orElseThrow(() -> new RuntimeException("Alocação não encontrada com ID: " + alocacaoId));

        String mensagem = mensagemPersonalizada != null && !mensagemPersonalizada.trim().isEmpty()
                ? mensagemPersonalizada
                : gerarMensagemPadrao(alocacao);

        NotificacaoEntity notificacao = criarNotificacaoEntity(
                alocacao.getColaborador().getId(),
                mensagem,
                "ALOCACAO_SHOW",
                alocacaoId
        );

        ResponseNotificacaoDto response = ResponseNotificacaoDto.toResponse(notificacao);

        // ✅ CORRIGIDO: Pegar email através da relação
        String username = obterUsernameDoColaborador(alocacao.getColaborador());
        if (username != null) {
            webSocketService.enviarNotificacaoParaColaborador(username, response);
        }

        return response;
    }

    // ✅ ATUALIZADO: Marcar como lida + atualizar contador via WebSocket
    @Transactional
    public ResponseNotificacaoDto marcarComoLida(Long id) {
        NotificacaoEntity notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada com ID: " + id));

        if (!notificacao.isLida()) {
            notificacao.setLida(true);
            notificacao = notificacaoRepository.save(notificacao);

            // ✅ CORRIGIDO: Enviar contador atualizado via WebSocket
            String username = obterUsernameDoColaborador(notificacao.getColaborador());
            if (username != null) {
                long novoContador = contarNaoLidas(notificacao.getColaborador().getId());
                webSocketService.enviarContadorParaColaborador(username, novoContador);
            }
        }

        return ResponseNotificacaoDto.toResponse(notificacao);
    }

    // ✅ ATUALIZADO: Marcar todas como lidas + WebSocket
    @Transactional
    public void marcarTodasComoLidas(Long colaboradorId) {
        List<NotificacaoEntity> notificacoesNaoLidas = notificacaoRepository.findByColaboradorIdAndLidaFalse(colaboradorId);

        if (!notificacoesNaoLidas.isEmpty()) {
            for (NotificacaoEntity notificacao : notificacoesNaoLidas) {
                notificacao.setLida(true);
            }

            notificacaoRepository.saveAll(notificacoesNaoLidas);

            // ✅ CORRIGIDO: Enviar contador zerado via WebSocket
            String username = obterUsernameDoColaborador(notificacoesNaoLidas.get(0).getColaborador());
            if (username != null) {
                webSocketService.enviarContadorParaColaborador(username, 0);
            }
        }
    }

    // ✅ NOVO: Método para enviar notificação de resposta de alocação
    @Transactional
    public void notificarRespostaAlocacao(Long alocacaoId, boolean aceito, String usernameGestor) {
        try {
            AlocacaoEntity alocacao = alocacaoRepository.findById(alocacaoId)
                    .orElseThrow(() -> new RuntimeException("Alocação não encontrada"));

            String nomeColaborador = alocacao.getColaborador().getNome();
            String nomeShow = alocacao.getShow().getNomeEvento();

            // Notificar gestor sobre a resposta
            webSocketService.enviarRespostaAlocacao(usernameGestor, nomeColaborador, nomeShow, aceito);

        } catch (Exception e) {
            // Log do erro, mas não falha a operação principal
            System.err.println("Erro ao enviar notificação WebSocket: " + e.getMessage());
        }
    }

    // ✅ NOVO: Método helper para obter username do colaborador
    private String obterUsernameDoColaborador(ColaboradorEntity colaborador) {
        try {
            if (colaborador.getCredenciais() != null) {
                // Retorna o email (que é usado como username no JWT)
                return colaborador.getCredenciais().getEmail();
            }
            return null;
        } catch (Exception e) {
            System.err.println("Erro ao obter username do colaborador: " + e.getMessage());
            return null;
        }
    }

    // ✅ Método interno para criar entidade
    private NotificacaoEntity criarNotificacaoEntity(Long colaboradorId, String mensagem, String tipo, Long alocacaoId) {
        ColaboradorEntity colaborador = colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado com ID: " + colaboradorId));

        NotificacaoEntity notificacao = new NotificacaoEntity();
        notificacao.setColaborador(colaborador);
        notificacao.setMensagem(mensagem);
        notificacao.setTipo(tipo);
        notificacao.setDataCriacao(LocalDateTime.now());

        // Vincular alocação se fornecida
        if (alocacaoId != null) {
            AlocacaoEntity alocacao = alocacaoRepository.findById(alocacaoId)
                    .orElseThrow(() -> new RuntimeException("Alocação não encontrada com ID: " + alocacaoId));
            notificacao.setAlocacao(alocacao);
        }

        return notificacaoRepository.save(notificacao);
    }

    // ✅ Método auxiliar para gerar mensagem padrão
    private String gerarMensagemPadrao(AlocacaoEntity alocacao) {
        String nomeEvento = alocacao.getShow() != null ? alocacao.getShow().getNomeEvento() : "evento";
        return String.format("Você foi alocado para o show '%s'. Clique para aceitar ou recusar.", nomeEvento);
    }

    // ✅ Listar notificações com dados completos
    @Transactional(readOnly = true)
    public List<ResponseNotificacaoDto> listarPorColaborador(Long colaboradorId) {
        List<NotificacaoEntity> notificacoes = notificacaoRepository.findByColaboradorIdOrderByDataCriacaoDesc(colaboradorId);
        return ResponseNotificacaoDto.toResponse(notificacoes);
    }

    @Transactional(readOnly = true)
    public List<ResponseNotificacaoDto> listarNaoLidas(Long colaboradorId) {
        List<NotificacaoEntity> notificacoes = notificacaoRepository.findByColaboradorIdAndLidaFalseOrderByDataCriacaoDesc(colaboradorId);
        return ResponseNotificacaoDto.toResponse(notificacoes);
    }

    // ✅ NOVO: Contar não lidas
    @Transactional(readOnly = true)
    public long contarNaoLidas(Long colaboradorId) {
        return notificacaoRepository.countByColaboradorIdAndLidaFalse(colaboradorId);
    }

    // ✅ Métodos de compatibilidade (mantidos para não quebrar código existente)
    @Transactional
    public NotificacaoEntity criarNotificacao(Long colaboradorId, String mensagem, String tipo) {
        return criarNotificacaoEntity(colaboradorId, mensagem, tipo, null);
    }

    @Transactional
    public NotificacaoEntity criarNotificacao(Long colaboradorId, String mensagem, String tipo, Long alocacaoId) {
        return criarNotificacaoEntity(colaboradorId, mensagem, tipo, alocacaoId);
    }

    public List<NotificacaoEntity> listarPorColaboradorEntity(Long colaboradorId) {
        return notificacaoRepository.findByColaboradorId(colaboradorId);
    }

    public List<NotificacaoEntity> listarNaoLidasEntity(Long colaboradorId) {
        return notificacaoRepository.findByColaboradorIdAndLidaFalse(colaboradorId);
    }

    @Transactional
    public NotificacaoEntity marcarComoLidaEntity(Long id) {
        NotificacaoEntity notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
        notificacao.setLida(true);
        return notificacaoRepository.save(notificacao);
    }
}