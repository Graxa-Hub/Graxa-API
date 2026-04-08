package com.Graxa_API.Graxa_API.messaging;

import com.Graxa_API.Graxa_API.core.application.gateway.EmailGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailAlocacaoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(EmailAlocacaoConsumer.class);

    private final EmailGateway emailGateway;

    public EmailAlocacaoConsumer(EmailGateway emailGateway) {
        this.emailGateway = emailGateway;
    }

    @RabbitListener(queues = "${graxa.rabbit.email-alocacao.queue}")
    public void consumir(EmailAlocacaoMessage message) {
        try {
            emailGateway.enviar(message.emailDestino(), message.assunto(), message.mensagem());
            logger.info("E-mail de alocação enviado com sucesso para colaboradorId={}", message.colaboradorId());
        } catch (Exception exception) {
            logger.error(
                    "Erro ao enviar e-mail de alocação para colaboradorId={}: {}",
                    message.colaboradorId(),
                    exception.getMessage(),
                    exception
            );
            throw exception;
        }
    }
}
