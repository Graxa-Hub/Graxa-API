package com.Graxa_API.Graxa_API.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusAlocacao {
    PENDENTE("pendente"),
    ACEITA("aceita"),
    RECUSADA("recusada");

    private final String value;

    StatusAlocacao(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static StatusAlocacao fromValue(String value) {
        for (StatusAlocacao status : StatusAlocacao.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Valor inválido para StatusAlocacao: " + value);
    }
}
