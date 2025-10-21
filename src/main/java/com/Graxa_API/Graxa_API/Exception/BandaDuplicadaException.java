package com.Graxa_API.Graxa_API.Exception;

public class BandaDuplicadaException extends RuntimeException {
    public BandaDuplicadaException(String banda) {
        super("A banda "+banda+ ", Já existe.");
    }
}
