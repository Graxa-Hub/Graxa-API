package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.AlocacaoEntity;
import com.Graxa_API.Graxa_API.Entity.NotificacaoEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Repository.AlocacaoRepository;
import com.Graxa_API.Graxa_API.Repository.ColaboradorRepository;
import com.Graxa_API.Graxa_API.Repository.NotificacaoRepository;
import com.Graxa_API.Graxa_API.core.application.gateway.EmailGateway;
import com.Graxa_API.Graxa_API.dto.NotificacaoDto.RequestNotificacaoDto;
import com.Graxa_API.Graxa_API.dto.NotificacaoDto.ResponseNotificacaoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacaoService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacaoService.class);

    private final NotificacaoRepository notificacaoRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final AlocacaoRepository alocacaoRepository;

    @Autowired
    private NotificacaoWebSocketService webSocketService;

    @Autowired
    private EmailGateway emailGateway;

    public NotificacaoService(NotificacaoRepository notificacaoRepository,
                              ColaboradorRepository colaboradorRepository,
                              AlocacaoRepository alocacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.alocacaoRepository = alocacaoRepository;
    }

    // ✅ ATUALIZADO: Criar notificação + WebSocket COM LOGS
    @Transactional
    public ResponseNotificacaoDto criarNotificacao(RequestNotificacaoDto request) {
        logger.info("📝 Criando notificação para colaboradorId={}, tipo={}", request.colaboradorId(), request.tipo());

        NotificacaoEntity notificacao = criarNotificacaoEntity(
                request.colaboradorId(),
                request.mensagem(),
                request.tipo(),
                request.alocacaoId()
        );

        ResponseNotificacaoDto response = ResponseNotificacaoDto.toResponse(notificacao);
        logger.info("💾 Notificação salva no banco: ID={}", notificacao.getId());

        // ✅ CORRIGIDO: Pegar email através da relação
        String username = obterUsernameDoColaborador(notificacao.getColaborador());
        if (username != null) {
            logger.info("👤 Username obtido: {} - Enviando via WebSocket...", username);
            webSocketService.enviarNotificacaoParaColaborador(username, response);

            // ✅ Enviar contador também
            long contador = contarNaoLidas(request.colaboradorId());
            logger.info("📊 Enviando contador: {} não lidas", contador);
            webSocketService.enviarContadorParaColaborador(username, contador);

            enviarEmailNotificacao(username, request.tipo(), request.mensagem());
        } else {
            logger.warn("⚠️ Username NULL para colaboradorId={}", request.colaboradorId());
        }

        return response;
    }

    // ✅ ATUALIZADO: Criar notificação de alocação + WebSocket COM LOGS
    @Transactional
    public ResponseNotificacaoDto criarNotificacaoAlocacao(Long alocacaoId, String mensagemPersonalizada) {
        logger.info("📝 Criando notificação de alocação para alocacaoId={}", alocacaoId);

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
        logger.info("💾 Notificação de alocação salva: ID={}", notificacao.getId());

        // ✅ CORRIGIDO: Pegar email através da relação
        String username = obterUsernameDoColaborador(alocacao.getColaborador());
        if (username != null) {
            logger.info("👤 Username obtido: {} - Enviando via WebSocket...", username);
            webSocketService.enviarNotificacaoParaColaborador(username, response);

            // ✅ Enviar contador também
            long contador = contarNaoLidas(alocacao.getColaborador().getId());
            logger.info("📊 Enviando contador: {} não lidas", contador);
            webSocketService.enviarContadorParaColaborador(username, contador);

            enviarEmailNotificacao(username, "ALOCACAO_SHOW", mensagem);
        } else {
            logger.warn("⚠️ Username NULL para alocação ID={}", alocacaoId);
        }

        return response;
    }

    // ✅ ATUALIZADO: Marcar como lida + atualizar contador via WebSocket COM LOGS
    @Transactional
    public ResponseNotificacaoDto marcarComoLida(Long id) {
        logger.info("✓ Marcando notificação ID={} como lida", id);

        NotificacaoEntity notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada com ID: " + id));

        if (!notificacao.isLida()) {
            notificacao.setLida(true);
            notificacao = notificacaoRepository.save(notificacao);
            logger.info("💾 Notificação marcada como lida no banco");

            // ✅ CORRIGIDO: Enviar contador atualizado via WebSocket
            String username = obterUsernameDoColaborador(notificacao.getColaborador());
            if (username != null) {
                long novoContador = contarNaoLidas(notificacao.getColaborador().getId());
                logger.info("📊 Atualizando contador para {}: {} não lidas", username, novoContador);
                webSocketService.enviarContadorParaColaborador(username, novoContador);
            }
        } else {
            logger.info("ℹ️ Notificação ID={} já estava lida", id);
        }

        return ResponseNotificacaoDto.toResponse(notificacao);
    }

    // ✅ ATUALIZADO: Marcar todas como lidas + WebSocket COM LOGS
    @Transactional
    public void marcarTodasComoLidas(Long colaboradorId) {
        logger.info("✓ Marcando todas as notificações como lidas para colaboradorId={}", colaboradorId);

        List<NotificacaoEntity> notificacoesNaoLidas = notificacaoRepository.findByColaboradorIdAndLidaFalse(colaboradorId);

        if (!notificacoesNaoLidas.isEmpty()) {
            logger.info("💾 Marcando {} notificações como lidas", notificacoesNaoLidas.size());

            for (NotificacaoEntity notificacao : notificacoesNaoLidas) {
                notificacao.setLida(true);
            }

            notificacaoRepository.saveAll(notificacoesNaoLidas);

            // ✅ CORRIGIDO: Enviar contador zerado via WebSocket
            String username = obterUsernameDoColaborador(notificacoesNaoLidas.get(0).getColaborador());
            if (username != null) {
                logger.info("📊 Enviando contador zerado para {}", username);
                webSocketService.enviarContadorParaColaborador(username, 0);
            }
        } else {
            logger.info("ℹ️ Nenhuma notificação não lida para colaboradorId={}", colaboradorId);
        }
    }

    // ✅ ATUALIZADO: Método para enviar notificação de resposta de alocação COM LOGS
    @Transactional
    public void notificarRespostaAlocacao(Long alocacaoId, boolean aceito, String usernameGestor) {
        try {
            logger.info("📨 Notificando resposta de alocação ID={}, aceito={}", alocacaoId, aceito);

            AlocacaoEntity alocacao = alocacaoRepository.findById(alocacaoId)
                    .orElseThrow(() -> new RuntimeException("Alocação não encontrada"));

            String nomeColaborador = alocacao.getColaborador().getNome();
            String nomeShow = alocacao.getShow().getNomeEvento();

            // Notificar gestor sobre a resposta
            webSocketService.enviarRespostaAlocacao(usernameGestor, nomeColaborador, nomeShow, aceito);
            logger.info("✅ Resposta de alocação enviada para {}", usernameGestor);

        } catch (Exception e) {
            logger.error("❌ Erro ao enviar notificação WebSocket: {}", e.getMessage(), e);
        }
    }

    // ✅ ATUALIZADO: Método helper para obter username do colaborador COM LOGS
    private String obterUsernameDoColaborador(ColaboradorEntity colaborador) {
        try {
            if (colaborador.getCredenciais() != null) {
                String email = colaborador.getCredenciais().getEmail();
                logger.debug("📧 Email/username obtido: {}", email);
                return email;
            }
            logger.warn("⚠️ Credenciais NULL para colaborador ID={}", colaborador.getId());
            return null;
        } catch (Exception e) {
            logger.error("❌ Erro ao obter username: {}", e.getMessage(), e);
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
        NotificacaoEntity notificacao = criarNotificacaoEntity(colaboradorId, mensagem, tipo, alocacaoId);
        String username = obterUsernameDoColaborador(notificacao.getColaborador());
        if (username != null) {
            ResponseNotificacaoDto response = ResponseNotificacaoDto.toResponse(notificacao);
            webSocketService.enviarNotificacaoParaColaborador(username, response);
            webSocketService.enviarContadorParaColaborador(username, contarNaoLidas(colaboradorId));
            enviarEmailNotificacao(username, tipo, mensagem);
        }
        return notificacao;
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

    private void enviarEmailNotificacao(String email, String tipo, String mensagem) {
        try {
            String assunto = resolverAssunto(tipo);

            String corpo = """
        Olá,

        Você recebeu uma nova notificação no sistema Graxa:

        📢 %s

        Acesse o sistema para mais detalhes.

        — Equipe Graxa
        """.formatted(mensagem);

            emailGateway.enviar(email, assunto, corpo);

            logger.info("📧 E-mail de notificação enviado para {}", email);
        } catch (Exception e) {
            logger.error("❌ Falha ao enviar e-mail de notificação para {}: {}", email, e.getMessage(), e);
        }
    }

    private String resolverAssunto(String tipo) {
        return switch (tipo) {
            case "ALOCACAO_SHOW" -> "Graxa - Você foi alocado para um show";
            default -> "Graxa - Nova notificação";
        };
    }
}