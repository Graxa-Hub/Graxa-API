package com.graxa.emailservice.listener;

import com.graxa.emailservice.config.RabbitMQConfig;
import com.graxa.emailservice.messaging.EmailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailListener {

    private static final Logger log = LoggerFactory.getLogger(EmailListener.class);

    private final JavaMailSender mailSender;

    public EmailListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receberEEnviar(EmailMessage message) {
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(message.para());
            email.setSubject(message.assunto());
            email.setText(message.mensagem());
            mailSender.send(email);
            log.info("E-mail enviado para: {}", message.para());
        } catch (Exception e) {
            log.error("Falha ao enviar e-mail para {}: {}", message.para(), e.getMessage());
            throw e;
        }
    }
}
