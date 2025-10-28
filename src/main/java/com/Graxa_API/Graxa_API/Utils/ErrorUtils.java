package com.Graxa_API.Graxa_API.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorUtils {

    public static ResponseEntity<Object> buildErrorResponse(HttpStatus status, String mensagem, List<Map<String, String>> erros) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("mensagem", mensagem);
        body.put("erros", erros);
        return ResponseEntity.status(status).body(body);
    }
}
