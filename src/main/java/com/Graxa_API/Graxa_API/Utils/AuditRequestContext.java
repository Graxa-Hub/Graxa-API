package com.Graxa_API.Graxa_API.Utils;

import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.UsuarioEntity;

public class AuditRequestContext {
    private static final ThreadLocal<AuditRequestContext> CONTEXT = new ThreadLocal<>();

    private String ip;
    private String endpoint;
    private String method;
    private ColaboradorEntity usuario;

    public static void set(AuditRequestContext ctx) { CONTEXT.set(ctx); }
    public static AuditRequestContext get() { return CONTEXT.get(); }
    public static void clear() { CONTEXT.remove(); }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ColaboradorEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(ColaboradorEntity usuario) {
        this.usuario = usuario;
    }
}

