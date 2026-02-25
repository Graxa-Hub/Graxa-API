package com.Graxa_API.Graxa_API.handler;

import com.Graxa_API.Graxa_API.Exception.*;
import com.Graxa_API.Graxa_API.Utils.ErrorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<Object> handleUsuarioNaoEncontradoException(UsuarioNaoEncontradoException e) {
        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "usuario");
        erro.put("mensagem", "Usuário não encontrado"); // mensagem controlada

        return ErrorUtils.buildErrorResponse(HttpStatus.NOT_FOUND, "Usuário não encontrado", List.of(erro));
    }

    @ExceptionHandler(io.github.resilience4j.ratelimiter.RequestNotPermitted.class)
    public ResponseEntity<String> handleRateLimiter(io.github.resilience4j.ratelimiter.RequestNotPermitted ex) {
        return ResponseEntity.status(429).body("Muitas tentativas de login. Tente novamente em alguns segundos.");
    }

    @ExceptionHandler(UsuariosNaoEncontradosException.class)
    public ResponseEntity<Object> handleUsuariosNaoEncontradosException(UsuariosNaoEncontradosException e) {
        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "usuario");
        erro.put("mensagem", "Nenhum usuário encontrado"); // mensagem controlada

        return ErrorUtils.buildErrorResponse(HttpStatus.NOT_FOUND, "Nenhum usuário encontrado", List.of(erro));
    }

    @ExceptionHandler(BandaNaoEncontradaException.class)
    public ResponseEntity<Object> handleBandaNaoEncontradaException(BandaNaoEncontradaException e) {
        Map<String, String> erro = new HashMap<>();
        erro.put("mensagem", "Banda não encontrada"); // mensagem controlada

        return ErrorUtils.buildErrorResponse(HttpStatus.NOT_FOUND, "Banda não encontrada", List.of(erro));
    }

    @ExceptionHandler(CpfDuplicadoException.class)
    public ResponseEntity<Object> handleCpfDuplicadoException(CpfDuplicadoException e) {
        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "cpf");
        erro.put("mensagem", "CPF já cadastrado"); // mensagem controlada

        return ErrorUtils.buildErrorResponse(HttpStatus.CONFLICT, "CPF duplicado", List.of(erro));
    }

    @ExceptionHandler(PathTraversalException.class)
    public ResponseEntity<Object> handlePathTraversalException(PathTraversalException e) {
        Map<String, String> erro = new HashMap<>();
        erro.put("mensagem", "Acesso inválido ao arquivo"); // mensagem controlada
        erro.put("codigo", "ERR_403");

        return ErrorUtils.buildErrorResponse(HttpStatus.FORBIDDEN, "Path Traversal detectado", List.of(erro));
    }


    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<Object> handleEmailDuplicadoException(EmailDuplicadoException e) {
        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "email");
        erro.put("mensagem", "Email já cadastrado"); // mensagem controlada

        return ErrorUtils.buildErrorResponse(HttpStatus.CONFLICT, "Email duplicado", List.of(erro));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    Map<String, String> err = new HashMap<>();
                    err.put("campo", error.getField());
                    err.put("mensagem", error.getDefaultMessage());
                    return err;
                })
                .toList();

        return ErrorUtils.buildErrorResponse(HttpStatus.BAD_REQUEST, "Erro de validação", errors);
    }

    // Handler genérico para qualquer outra exceção não tratada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception e) {
        log.error("Erro inesperado", e); // log interno com stacktrace

        Map<String, String> erro = new HashMap<>();
        erro.put("mensagem", "Erro interno"); // mensagem genérica
        erro.put("codigo", "ERR_500");

        return ErrorUtils.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno", List.of(erro));
    }
}
