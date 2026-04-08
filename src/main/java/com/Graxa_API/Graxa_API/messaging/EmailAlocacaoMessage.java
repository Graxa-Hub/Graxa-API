package com.Graxa_API.Graxa_API.messaging;

public record EmailAlocacaoMessage(
        Long colaboradorId,
        String emailDestino,
        String nomeColaborador,
        String nomeShow,
        String assunto,
        String mensagem
) {
}
