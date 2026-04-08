package com.Graxa_API.Graxa_API.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${graxa.rabbit.email-alocacao.queue}")
    private String emailAlocacaoQueue;

    @Value("${graxa.rabbit.email-alocacao.exchange}")
    private String emailAlocacaoExchange;

    @Value("${graxa.rabbit.email-alocacao.routing-key}")
    private String emailAlocacaoRoutingKey;

    @Bean
    public Queue emailAlocacaoQueue() {
        return new Queue(emailAlocacaoQueue, true);
    }

    @Bean
    public DirectExchange emailAlocacaoExchange() {
        return new DirectExchange(emailAlocacaoExchange);
    }

    @Bean
    public Binding emailAlocacaoBinding(Queue emailAlocacaoQueue, DirectExchange emailAlocacaoExchange) {
        return BindingBuilder.bind(emailAlocacaoQueue)
                .to(emailAlocacaoExchange)
                .with(emailAlocacaoRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
