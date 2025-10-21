package com.Graxa_API.Graxa_API.Exception;

public class BandaNaoEncontradaException extends RuntimeException {
    public BandaNaoEncontradaException(Long id) {
        super("Banda com o id: "+ id+", Não encontrado.");
    }
    public BandaNaoEncontradaException(String nome) {
        super("Banda "+nome+ " Não Encontrada.");
    }
}
