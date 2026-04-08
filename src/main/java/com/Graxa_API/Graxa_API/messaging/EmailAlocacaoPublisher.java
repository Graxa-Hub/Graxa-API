package com.Graxa_API.Graxa_API.messaging;

import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailAlocacaoPublisher {

    private static final Logger logger = LoggerFactory.getLogger(EmailAlocacaoPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public EmailAlocacaoPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${graxa.rabbit.email-alocacao.exchange}") String exchange,
            @Value("${graxa.rabbit.email-alocacao.routing-key}") String routingKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void publicarEmailDeAlocacao(ColaboradorEntity colaborador, ShowEntity show) {
        if (colaborador.getCredenciais() == null || colaborador.getCredenciais().getEmail() == null) {
            logger.warn("Não foi possível publicar e-mail de alocação: colaborador sem e-mail. colaboradorId={}", colaborador.getId());
            return;
        }

        EmailAlocacaoMessage mensagem = new EmailAlocacaoMessage(
                colaborador.getId(),
                colaborador.getCredenciais().getEmail(),
                colaborador.getNome(),
                show.getNomeEvento(),
                "Nova alocação de show",
                String.format(
                        "Olá, %s!%n%nVocê foi alocado para o show \"%s\".%nAcesse a plataforma para aceitar ou recusar a alocação.",
                        colaborador.getNome(),
                        show.getNomeEvento()
                )
        );

        rabbitTemplate.convertAndSend(exchange, routingKey, mensagem);
        logger.info("Mensagem de e-mail de alocação publicada na fila para colaboradorId={}", colaborador.getId());
    }
}
