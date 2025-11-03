package com.Graxa_API.Graxa_API.Exception;

public class LoginInvalidoException extends RuntimeException {
    public LoginInvalidoException() {
        super("Identificador ou senha incorretos.");
    }
}
