package com.Graxa_API.Graxa_API.Aspect;

import com.Graxa_API.Graxa_API.Entity.Identifiable;
import com.Graxa_API.Graxa_API.Utils.AuditRequestContext;
import com.Graxa_API.Graxa_API.Service.AuditLogService;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class AuditAspect {

    @Autowired
    private AuditLogService auditLogService;

    @AfterReturning(pointcut = "execution(* com.Graxa_API.Graxa_API.Repository.*.save(..))", returning = "entity")
    public void logSave(Object entity) {
        AuditRequestContext ctx = AuditRequestContext.get();
        if (entity instanceof Identifiable && ctx != null && ctx.getUsuario() != null) {
            if (ctx.getEndpoint() != null && ctx.getEndpoint().contains("/login")) {
                return;
            }

            Long id = ((Identifiable) entity).getId();
            auditLogService.log(
                    "SAVE",
                    entity.getClass().getSimpleName(),
                    String.valueOf(id),
                    ctx.getUsuario(),
                    ctx.getIp(),
                    "Objeto salvo/atualizado em " + ctx.getEndpoint()
            );
        }
    }

    @After("execution(* com.Graxa_API.Graxa_API.Repository.*.deleteById(..)) && args(id,..)")
    public void logDelete(Long id) {
        AuditRequestContext ctx = AuditRequestContext.get();
        if (ctx != null && ctx.getUsuario() != null) {
            auditLogService.log(
                    "DELETE",
                    "Entidade",
                    String.valueOf(id),
                    ctx.getUsuario(),
                    ctx.getIp(),
                    "Objeto deletado em " + ctx.getEndpoint()
            );
        }
    }

    @After("execution(* com.Graxa_API.Graxa_API.Repository.*.delete(..)) && args(entity,..)")
    public void logDeleteEntity(Object entity) {
        AuditRequestContext ctx = AuditRequestContext.get();
        if (entity instanceof Identifiable && ctx != null && ctx.getUsuario() != null) {
            Long id = ((Identifiable) entity).getId();
            auditLogService.log(
                    "DELETE",
                    entity.getClass().getSimpleName(),
                    String.valueOf(id),
                    ctx.getUsuario(),
                    ctx.getIp(),
                    "Objeto deletado em " + ctx.getEndpoint()
            );
        }
    }
}

