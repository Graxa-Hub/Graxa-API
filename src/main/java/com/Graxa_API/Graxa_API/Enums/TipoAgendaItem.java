package com.Graxa_API.Graxa_API.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoAgendaItem {
    DESLOCAMENTO("deslocamento"),
    TECNICO("tecnico");

    private final String value;

    TipoAgendaItem(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TipoAgendaItem fromValue(String value) {
        for (TipoAgendaItem tipo : TipoAgendaItem.values()) {
            if (tipo.value.equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Valor inválido para TipoAgendaItem: " + value);
    }
}
