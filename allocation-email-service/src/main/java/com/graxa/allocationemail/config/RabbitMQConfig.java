package com.graxa.allocationemail.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String ALOCACAO_EXCHANGE = "alocacao.exchange";
    public static final String ALOCACAO_QUEUE = "alocacao.email.queue";
    public static final String ALOCACAO_DLQ = "alocacao.email.dlq";
    public static final String ALOCACAO_ROUTING_KEY = "alocacao.show.criada";
    public static final String ALOCACAO_DLQ_ROUTING_KEY = "alocacao.show.criada.dlq";

    @Bean
    public DirectExchange alocacaoExchange() {
        return new DirectExchange(ALOCACAO_EXCHANGE, true, false);
    }

    @Bean
    public Queue alocacaoQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", ALOCACAO_EXCHANGE);
        args.put("x-dead-letter-routing-key", ALOCACAO_DLQ_ROUTING_KEY);
        return new Queue(ALOCACAO_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue alocacaoDlq() {
        return new Queue(ALOCACAO_DLQ, true);
    }

    @Bean
    public Binding alocacaoBinding(Queue alocacaoQueue, DirectExchange alocacaoExchange) {
        return BindingBuilder.bind(alocacaoQueue).to(alocacaoExchange).with(ALOCACAO_ROUTING_KEY);
    }

    @Bean
    public Binding alocacaoDlqBinding(Queue alocacaoDlq, DirectExchange alocacaoExchange) {
        return BindingBuilder.bind(alocacaoDlq).to(alocacaoExchange).with(ALOCACAO_DLQ_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setDefaultRequeueRejected(false);
        factory.setAdviceChain(
                RetryInterceptorBuilder.stateless()
                        .maxAttempts(3)
                        .backOffOptions(1_000, 2.0, 10_000)
                        .recoverer(new RejectAndDontRequeueRecoverer())
                        .build()
        );
        return factory;
    }
}
