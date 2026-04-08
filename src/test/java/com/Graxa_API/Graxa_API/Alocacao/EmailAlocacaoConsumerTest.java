package com.Graxa_API.Graxa_API.Alocacao;

import com.Graxa_API.Graxa_API.core.application.gateway.EmailGateway;
import com.Graxa_API.Graxa_API.messaging.EmailAlocacaoConsumer;
import com.Graxa_API.Graxa_API.messaging.EmailAlocacaoMessage;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class EmailAlocacaoConsumerTest {

    @Test
    void deveConsumirMensagemEEnviarEmail() {
        EmailGateway emailGateway = mock(EmailGateway.class);
        EmailAlocacaoConsumer consumer = new EmailAlocacaoConsumer(emailGateway);

        EmailAlocacaoMessage message = new EmailAlocacaoMessage(
                1L,
                "destino@graxa.com",
                "Colaborador",
                "Show Teste",
                "Nova alocação de show",
                "Mensagem"
        );

        consumer.consumir(message);

        verify(emailGateway).enviar("destino@graxa.com", "Nova alocação de show", "Mensagem");
    }
}
