package com.Graxa_API.Graxa_API.infra.messaging;

import com.Graxa_API.Graxa_API.core.application.gateway.EmailGateway;
import com.Graxa_API.Graxa_API.infra.config.RabbitMQEmailConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQEmailGateway implements EmailGateway {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEmailGateway(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void enviar(String para, String assunto, String mensagem) {
        rabbitTemplate.convertAndSend(
                RabbitMQEmailConfig.EXCHANGE,
                RabbitMQEmailConfig.ROUTING_KEY,
                new EmailMessage(para, assunto, mensagem)
        );
    }
}
