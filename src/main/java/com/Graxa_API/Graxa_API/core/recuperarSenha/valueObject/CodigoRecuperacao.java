package com.Graxa_API.Graxa_API.core.recuperarSenha.valueObject;

import java.time.LocalDateTime;

public final class CodigoRecuperacao {

    private final String valor;
    private final LocalDateTime expiraEm;

    private CodigoRecuperacao(String valor, LocalDateTime expiraEm) {
        this.valor = valor;
        this.expiraEm = expiraEm;
    }

    public static CodigoRecuperacao gerar() {
        String codigo = String.valueOf((int) (Math.random() * 900000) + 100000);
        return new CodigoRecuperacao(codigo, LocalDateTime.now().plusMinutes(10));
    }

    public static CodigoRecuperacao de(String valor, LocalDateTime expiraEm) {
        return new CodigoRecuperacao(valor, expiraEm);
    }

    public boolean expirou() {
        return LocalDateTime.now().isAfter(this.expiraEm);
    }

    public boolean correspondeA(String codigoInformado) {
        return this.valor.equals(codigoInformado);
    }

    public String getValor() { return valor; }
    public LocalDateTime getExpiraEm() { return expiraEm; }
}