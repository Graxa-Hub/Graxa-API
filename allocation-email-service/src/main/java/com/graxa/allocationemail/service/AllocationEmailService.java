package com.graxa.allocationemail.service;

import com.graxa.allocationemail.dto.AlocacaoShowEmailEventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class AllocationEmailService {

    private static final Logger log = LoggerFactory.getLogger(AllocationEmailService.class);

    private final JavaMailSender mailSender;

    @Value("${notification.mail.from}")
    private String from;

    public AllocationEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAllocationEmail(AlocacaoShowEmailEventDto event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(event.email());
        message.setSubject("Alocação confirmada para show");
        message.setText(buildEmailBody(event));

        mailSender.send(message);

        log.info("E-mail de alocação enviado para userId={} showId={}", event.userId(), event.showId());
    }

    private String buildEmailBody(AlocacaoShowEmailEventDto event) {
        return "Olá!\n\n" +
                "Você foi alocado para o show: " + event.showName() + "\n" +
                "Data do show: " + event.date() + "\n" +
                "Código da alocação (show): " + event.showId() + "\n\n" +
                "Atenciosamente,\n" +
                "Equipe Graxa";
    }
}
