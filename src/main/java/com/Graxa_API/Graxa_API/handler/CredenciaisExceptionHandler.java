package com.Graxa_API.Graxa_API.handler;

import com.Graxa_API.Graxa_API.Exception.CredencialNaoEncontradaException;
import com.Graxa_API.Graxa_API.Exception.LoginInvalidoException;
import com.Graxa_API.Graxa_API.Exception.NomeUsuarioDuplicadoException;
import com.Graxa_API.Graxa_API.Exception.SenhaInvalidaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CredenciaisExceptionHandler {

    @ExceptionHandler(CredencialNaoEncontradaException.class)
    public ResponseEntity<Object> handleCredencialNaoEncontrada(CredencialNaoEncontradaException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("mensagem", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(SenhaInvalidaException.class)
    public ResponseEntity<Object> handleSenhaInvalida(SenhaInvalidaException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("mensagem", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(LoginInvalidoException.class)
    public ResponseEntity<Object> handleLoginInvalido(LoginInvalidoException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("mensagem", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(NomeUsuarioDuplicadoException.class)
    public ResponseEntity<Object> handleNomeUsuarioDuplicado(NomeUsuarioDuplicadoException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("mensagem", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}
