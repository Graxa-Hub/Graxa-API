package com.Graxa_API.Graxa_API.handler;

import com.Graxa_API.Graxa_API.Exception.CredencialNaoEncontradaException;
import com.Graxa_API.Graxa_API.Exception.LoginInvalidoException;
import com.Graxa_API.Graxa_API.Exception.NomeUsuarioDuplicadoException;
import com.Graxa_API.Graxa_API.Exception.SenhaInvalidaException;
import com.Graxa_API.Graxa_API.Utils.ErrorUtils;
import com.Graxa_API.Graxa_API.core.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CredenciaisExceptionHandler {

    @ExceptionHandler(CredencialNaoEncontradaException.class)
    public ResponseEntity<Object> handleCredencialNaoEncontrada(CredencialNaoEncontradaException e) {
        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "credencial");
        erro.put("mensagem", e.getMessage());

        return ErrorUtils.buildErrorResponse(HttpStatus.NOT_FOUND, "Erro de credencial", List.of(erro));
    }

    @ExceptionHandler(SenhaInvalidaException.class)
    public ResponseEntity<Object> handleSenhaInvalida(SenhaInvalidaException e) {
        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "senha");
        erro.put("mensagem", e.getMessage());

        return ErrorUtils.buildErrorResponse(HttpStatus.UNAUTHORIZED, "Senha inválida", List.of(erro));
    }

    @ExceptionHandler(LoginInvalidoException.class)
    public ResponseEntity<Object> handleLoginInvalido(LoginInvalidoException e) {
        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "login");
        erro.put("mensagem", e.getMessage());

        return ErrorUtils.buildErrorResponse(HttpStatus.UNAUTHORIZED, "Login inválido", List.of(erro));
    }

    @ExceptionHandler(EmailNaoEncontradoException.class)
    public ResponseEntity<Object> handleEmailNaoEncontrado(EmailNaoEncontradoException e) {
        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "email");
        erro.put("mensagem", e.getMessage());

        return ErrorUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, "E-mail inválido", List.of(erro));
    }

    @ExceptionHandler(CodigoNaoSolicitadoException.class)
    public ResponseEntity<Object> handleCodigoNaoSolicitado(CodigoNaoSolicitadoException e) {
        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "codigo");
        erro.put("mensagem", e.getMessage());

        return ErrorUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, "Nenhum código solicitado", List.of(erro));
    }

    @ExceptionHandler(CodigoInvalidoException.class)
    public ResponseEntity<Object> handleCodigoInvalido(CodigoInvalidoException e) {
        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "codigo");
        erro.put("mensagem", e.getMessage());

        return ErrorUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, "Código inválido", List.of(erro));
    }

    @ExceptionHandler(CodigoExpiradoException.class)
    public ResponseEntity<Object> handleCodigoExpirado(CodigoExpiradoException e) {
        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "codigo");
        erro.put("mensagem", e.getMessage());

        return ErrorUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, "Código expirado", List.of(erro));
    }

    @ExceptionHandler(NomeUsuarioDuplicadoException.class)
    public ResponseEntity<Object> handleNomeUsuarioDuplicado(NomeUsuarioDuplicadoException e) {
        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "nomeUsuario");
        erro.put("mensagem", e.getMessage());

        return ErrorUtils.buildErrorResponse(HttpStatus.CONFLICT, "Nome de usuário duplicado", List.of(erro));
    }
}
