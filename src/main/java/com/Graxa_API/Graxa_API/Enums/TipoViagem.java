package com.Graxa_API.Graxa_API.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoViagem {
    AEREO("aereo"),
    TERRESTRE("terrestre"),
    ONIBUS("onibus"),
    CARRO("carro"),
    VAN("van");
    private final String value;
    TipoViagem(String value) {
        this.value = value;
    }
    @JsonValue
    public String getValue() {
        return value;
    }
    @JsonCreator
    public static TipoViagem fromValue(String value) {
        for (TipoViagem tipo : TipoViagem.values()) {
            if (tipo.value.equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Valor inválido para TipoUsuario: " + value);
    }
}
