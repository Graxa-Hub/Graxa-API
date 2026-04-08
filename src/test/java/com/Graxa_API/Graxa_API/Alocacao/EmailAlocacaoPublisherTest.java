package com.Graxa_API.Graxa_API.Alocacao;

import com.Graxa_API.Graxa_API.Entity.CredenciaisUsuarioEntity;
import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.messaging.EmailAlocacaoPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class EmailAlocacaoPublisherTest {

    private RabbitTemplate rabbitTemplate;
    private EmailAlocacaoPublisher publisher;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        publisher = new EmailAlocacaoPublisher(rabbitTemplate, "graxa.exchange", "graxa.routing");
    }

    @Test
    void devePublicarMensagemQuandoColaboradorPossuiEmail() {
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setId(10L);
        colaborador.setNome("Colaborador A");

        CredenciaisUsuarioEntity credenciais = new CredenciaisUsuarioEntity();
        credenciais.setEmail("colab@graxa.com");
        colaborador.setCredenciais(credenciais);

        ShowEntity show = new ShowEntity();
        show.setNomeEvento("Show Teste");

        publisher.publicarEmailDeAlocacao(colaborador, show);

        verify(rabbitTemplate).convertAndSend(eq("graxa.exchange"), eq("graxa.routing"), any());
    }

    @Test
    void naoDevePublicarMensagemQuandoColaboradorNaoTemEmail() {
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setId(11L);
        colaborador.setNome("Sem Email");

        ShowEntity show = new ShowEntity();
        show.setNomeEvento("Show Teste");

        publisher.publicarEmailDeAlocacao(colaborador, show);

        verify(rabbitTemplate, never()).convertAndSend(any(), any(), any());
    }
}
