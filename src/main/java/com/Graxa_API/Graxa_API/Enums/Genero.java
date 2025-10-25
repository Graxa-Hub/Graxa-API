package com.Graxa_API.Graxa_API.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Genero {
    rock("rock"),
    mpb("mpb"),
    alternativo("alternativo");
    private final String value;


    Genero(String value) {
        this.value = value;
    }

    @JsonValue  // Serializa como string (ex.: "produtor")
    public String getValue() {
        return value;
    }
    @JsonCreator  // Desserializa do JSON (ex.: "produtor" vira TipoUsuario.produtor)
    public static Genero fromValue(String value) {
        for (Genero tipo : Genero.values()) {
            if (tipo.value.equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Valor inválido para TipoUsuario: " + value);
    }
}
