package com.Graxa_API.Graxa_API.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoEndereco {
    LOCAL("local"),
    USUARIO("usuario");

    private final String value;

    TipoEndereco(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TipoEndereco fromValue(String value) {
        for (TipoEndereco tipo : TipoEndereco.values()) {
            if (tipo.value.equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Valor inválido para TipoEndereco: " + value);
    }
}
