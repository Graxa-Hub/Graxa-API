package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.AuditLog;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.UsuarioEntity;
import com.Graxa_API.Graxa_API.Repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository repo;

    public void log(String action,
                    String entity,
                    String entityId,
                    ColaboradorEntity usuario,
                    String ip,
                    String details) {

        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntity(entity);
        log.setEntityId(entityId);
        log.setUsuario(usuario);
        log.setIpAddress(ip);
        log.setTimestamp(LocalDateTime.now());
        log.setDetails(details);

        repo.save(log);
    }
}

