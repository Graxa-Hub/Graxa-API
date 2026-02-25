package com.Graxa_API.Graxa_API.handler;

import com.Graxa_API.Graxa_API.Exception.CredencialNaoEncontradaException;
import com.Graxa_API.Graxa_API.Exception.LoginInvalidoException;
import com.Graxa_API.Graxa_API.Exception.NomeUsuarioDuplicadoException;
import com.Graxa_API.Graxa_API.Exception.SenhaInvalidaException;
import com.Graxa_API.Graxa_API.Utils.ErrorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CredenciaisExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CredenciaisExceptionHandler.class);

    @ExceptionHandler(CredencialNaoEncontradaException.class)
    public ResponseEntity<Object> handleCredencialNaoEncontrada(CredencialNaoEncontradaException e) {
        log.error("Credencial não encontrada", e); // log interno

        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "credencial");
        erro.put("mensagem", "Credencial não encontrada"); // mensagem controlada

        return ErrorUtils.buildErrorResponse(HttpStatus.NOT_FOUND, "Erro de credencial", List.of(erro));
    }

    @ExceptionHandler(SenhaInvalidaException.class)
    public ResponseEntity<Object> handleSenhaInvalida(SenhaInvalidaException e) {
        log.warn("Tentativa de login com senha inválida", e); // log interno

        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "senha");
        erro.put("mensagem", "Senha inválida"); // mensagem controlada

        return ErrorUtils.buildErrorResponse(HttpStatus.UNAUTHORIZED, "Senha inválida", List.of(erro));
    }

    @ExceptionHandler(LoginInvalidoException.class)
    public ResponseEntity<Object> handleLoginInvalido(LoginInvalidoException e) {
        log.warn("Tentativa de login inválida", e); // log interno

        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "login");
        erro.put("mensagem", "Login inválido"); // mensagem controlada

        return ErrorUtils.buildErrorResponse(HttpStatus.UNAUTHORIZED, "Login inválido", List.of(erro));
    }

    @ExceptionHandler(NomeUsuarioDuplicadoException.class)
    public ResponseEntity<Object> handleNomeUsuarioDuplicado(NomeUsuarioDuplicadoException e) {
        log.error("Nome de usuário duplicado", e); // log interno

        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "nomeUsuario");
        erro.put("mensagem", "Nome de usuário já cadastrado"); // mensagem controlada

        return ErrorUtils.buildErrorResponse(HttpStatus.CONFLICT, "Nome de usuário duplicado", List.of(erro));
    }
}
