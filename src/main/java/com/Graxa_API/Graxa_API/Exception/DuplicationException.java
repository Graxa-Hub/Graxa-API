package com.Graxa_API.Graxa_API.Exception;

public class DuplicationException extends RuntimeException {
    public DuplicationException(String message) {
        super(message+ " já existe");
    }
}
