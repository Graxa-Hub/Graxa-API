package com.Graxa_API.Graxa_API.Enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
public enum TipoTelefone {
    celular("celular"),
    fixo("fixo");
    private final String value;  // Campo para armazenar o valor como string

    TipoTelefone(String value) {
        this.value = value;  // Agora armazena o valor passado
    }

    @JsonValue  // Serializa como string (ex.: "celular")
    public String getValue() {
        return value;
    }

    @JsonCreator  // Desserializa do JSON (ex.: "celular" vira TipoTelefone.celular)
    public static TipoTelefone fromValue(String value) {
        for (TipoTelefone tipo : TipoTelefone.values()) {
            if (tipo.value.equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Valor inválido para TipoTelefone: " + value);
    }
}