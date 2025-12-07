package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.dto.NotificacaoDto.ResponseNotificacaoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificacaoWebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacaoWebSocketService.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // ✅ CORRIGIDO: Envia notificação para um colaborador específico
    public void enviarNotificacaoParaColaborador(String username, ResponseNotificacaoDto notificacao) {
        try {
            logger.info("🔔 Enviando notificação WebSocket para {}: {}", username, notificacao.mensagem());

            // ✅ Envia para o tópico pessoal do usuário (/user/queue/notificacoes)
            messagingTemplate.convertAndSendToUser(
                    username,
                    "/queue/notificacoes",
                    notificacao
            );

            logger.info("✅ Notificação enviada com sucesso para {}", username);

        } catch (Exception e) {
            logger.error("❌ Erro ao enviar notificação WebSocket para {}: {}", username, e.getMessage(), e);
        }
    }

    // ✅ CORRIGIDO: Envia atualização de contador para um colaborador
    public void enviarContadorParaColaborador(String username, long contadorNaoLidas) {
        try {
            logger.info("📊 Enviando contador WebSocket para {}: {} não lidas", username, contadorNaoLidas);

            Map<String, Object> update = Map.of(
                    "naoLidas", contadorNaoLidas,
                    "timestamp", System.currentTimeMillis()
            );

            // ✅ Envia para /user/queue/contador
            messagingTemplate.convertAndSendToUser(
                    username,
                    "/queue/contador",
                    update
            );

            logger.info("✅ Contador enviado com sucesso para {}", username);

        } catch (Exception e) {
            logger.error("❌ Erro ao enviar contador WebSocket para {}: {}", username, e.getMessage(), e);
        }
    }

    // ✅ Envia para todos os colaboradores (broadcast)
    public void enviarParaTodos(String mensagem) {
        try {
            logger.info("📢 Enviando broadcast WebSocket");

            Map<String, Object> broadcast = Map.of(
                    "tipo", "broadcast",
                    "mensagem", mensagem,
                    "timestamp", System.currentTimeMillis()
            );

            messagingTemplate.convertAndSend("/topic/broadcast", broadcast);

            logger.info("✅ Broadcast enviado com sucesso");

        } catch (Exception e) {
            logger.error("❌ Erro ao enviar broadcast WebSocket: {}", e.getMessage(), e);
        }
    }

    // ✅ Envia notificação de alocação aceita/recusada
    public void enviarRespostaAlocacao(String username, String nomeColaborador, String nomeShow, boolean aceito) {
        try {
            logger.info("📨 Enviando resposta de alocação para {}", username);

            String mensagem = String.format(
                    "%s %s a alocação para o show '%s'",
                    nomeColaborador,
                    aceito ? "aceitou" : "recusou",
                    nomeShow
            );

            Map<String, Object> resposta = Map.of(
                    "tipo", "resposta_alocacao",
                    "mensagem", mensagem,
                    "aceito", aceito,
                    "colaborador", nomeColaborador,
                    "show", nomeShow,
                    "timestamp", System.currentTimeMillis()
            );

            messagingTemplate.convertAndSendToUser(
                    username,
                    "/queue/alocacao-resposta",
                    resposta
            );

            logger.info("✅ Resposta de alocação enviada com sucesso para {}", username);

        } catch (Exception e) {
            logger.error("❌ Erro ao enviar resposta de alocação WebSocket: {}", e.getMessage(), e);
        }
    }
}