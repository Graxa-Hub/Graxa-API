package com.Graxa_API.Graxa_API.handler;

import com.Graxa_API.Graxa_API.Exception.CpfDuplicadoException;
import com.Graxa_API.Graxa_API.Exception.EmailDuplicadoException;
import com.Graxa_API.Graxa_API.Exception.UsuarioNaoEncontradoException;
import com.Graxa_API.Graxa_API.Exception.UsuariosNaoEncontradosException;
import com.Graxa_API.Graxa_API.Utils.ErrorUtils;
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

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<Object> handleUsuarioNaoEncontradoException(UsuarioNaoEncontradoException e) {
        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "usuario");
        erro.put("mensagem", e.getMessage());

        return ErrorUtils.buildErrorResponse(HttpStatus.NOT_FOUND, "Usuário não encontrado", List.of(erro));
    }

    @ExceptionHandler(UsuariosNaoEncontradosException.class)
    public ResponseEntity<Object> handleUsuariosNaoEncontradosException(UsuariosNaoEncontradosException e) {
        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "usuario");
        erro.put("mensagem", e.getMessage());

        return ErrorUtils.buildErrorResponse(HttpStatus.NOT_FOUND, "Nenhum usuário encontrado", List.of(erro));
    }

    @ExceptionHandler(CpfDuplicadoException.class)
    public ResponseEntity<Object> handleCpfDuplicadoException(CpfDuplicadoException e) {
        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "cpf");
        erro.put("mensagem", e.getMessage());

        return ErrorUtils.buildErrorResponse(HttpStatus.CONFLICT, "CPF duplicado", List.of(erro));
    }

    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<Object> handleEmailDuplicadoException(EmailDuplicadoException e) {
        Map<String, String> erro = new HashMap<>();
        erro.put("campo", "email");
        erro.put("mensagem", e.getMessage());

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
}
