package com.Graxa_API.Graxa_API.Service.messaging;

import com.Graxa_API.Graxa_API.Config.RabbitMQAlocacaoConfig;
import com.Graxa_API.Graxa_API.dto.AlocacaoDto.AlocacaoShowEmailEventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlocacaoShowEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AlocacaoShowEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public AlocacaoShowEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarEventoAlocacao(AlocacaoShowEmailEventDto payload) {
        rabbitTemplate.convertAndSend(
                RabbitMQAlocacaoConfig.ALOCACAO_EXCHANGE,
                RabbitMQAlocacaoConfig.ALOCACAO_ROUTING_KEY,
                payload
        );

        log.info("Evento de alocação publicado para usuário {} no show {}",
                payload.userId(),
                payload.showId());
    }
}
