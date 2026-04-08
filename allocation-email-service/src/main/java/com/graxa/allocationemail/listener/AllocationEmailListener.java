package com.graxa.allocationemail.listener;

import com.graxa.allocationemail.config.RabbitMQConfig;
import com.graxa.allocationemail.dto.AlocacaoShowEmailEventDto;
import com.graxa.allocationemail.service.AllocationEmailService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AllocationEmailListener {

    private static final Logger log = LoggerFactory.getLogger(AllocationEmailListener.class);

    private final AllocationEmailService allocationEmailService;
    private final Validator validator;

    public AllocationEmailListener(AllocationEmailService allocationEmailService, Validator validator) {
        this.allocationEmailService = allocationEmailService;
        this.validator = validator;
    }

    @RabbitListener(queues = RabbitMQConfig.ALOCACAO_QUEUE)
    public void handleAllocationEvent(AlocacaoShowEmailEventDto event) {
        validate(event);

        try {
            allocationEmailService.sendAllocationEmail(event);
        } catch (Exception ex) {
            log.error("Falha ao processar evento de alocação. userId={} showId={} erro={}",
                    event.userId(), event.showId(), ex.getMessage(), ex);
            throw ex;
        }
    }

    private void validate(AlocacaoShowEmailEventDto event) {
        Set<ConstraintViolation<AlocacaoShowEmailEventDto>> violations = validator.validate(event);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
