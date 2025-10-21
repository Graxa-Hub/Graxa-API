package com.Graxa_API.Graxa_API.Exception;

public class BandasNaoEncontradasException extends RuntimeException {
    public BandasNaoEncontradasException() {
        super("Não há bandas salvas");
    }
}
