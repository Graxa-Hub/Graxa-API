package com.Graxa_API.Graxa_API.Enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
public enum TipoUsuario {
    PRODUTOR("produtor"),
    MUSICO("musico");
    private final String value;
    TipoUsuario(String value) {
        this.value = value;
    }
    @JsonValue  // Serializa como string (ex.: "produtor")
    public String getValue() {
        return value;
    }
    @JsonCreator  // Desserializa do JSON (ex.: "produtor" vira TipoUsuario.produtor)
    public static TipoUsuario fromValue(String value) {
        for (TipoUsuario tipo : TipoUsuario.values()) {
            if (tipo.value.equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Valor inválido para TipoUsuario: " + value);
    }
}