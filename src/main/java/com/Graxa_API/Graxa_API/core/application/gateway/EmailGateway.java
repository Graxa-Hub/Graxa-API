package com.Graxa_API.Graxa_API.core.application.gateway;

public interface EmailGateway {
    void enviar(String para, String assunto, String mensagem);
}
