package com.Graxa_API.Graxa_API.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoEvento {
        VIAGEM("viagem"),
        SHOW("show");
        private final String value;
        TipoEvento(String value) {
            this.value = value;
        }
        @JsonValue
        public String getValue() {
            return value;
        }
        @JsonCreator
        public static com.Graxa_API.Graxa_API.Enums.TipoEvento fromValue(String value) {
            for (com.Graxa_API.Graxa_API.Enums.TipoEvento tipo : com.Graxa_API.Graxa_API.Enums.TipoEvento.values()) {
                if (tipo.value.equalsIgnoreCase(value)) {
                    return tipo;
                }
            }
            throw new IllegalArgumentException("Valor inválido para TipoUsuario: " + value);
        }

}
