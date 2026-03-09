package com.Graxa_API.Graxa_API.infra.web;

import com.Graxa_API.Graxa_API.core.application.RecuperarSenhaUseCase;
import com.Graxa_API.Graxa_API.core.application.dto.RecuperarSenhaRequest;
import com.Graxa_API.Graxa_API.core.application.dto.ResetarSenhaRequest;
import com.Graxa_API.Graxa_API.core.application.dto.ValidarCodigoRequest;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credenciais")
@Tag(name = "Recuperação de Senha", description = "Endpoints para recuperação de senha")
public class RecuperarSenhaController {

    private final RecuperarSenhaUseCase useCase;

    public RecuperarSenhaController(RecuperarSenhaUseCase useCase) {
        this.useCase = useCase;
    }

    @Operation(summary = "Envia código de recuperação para o e-mail informado")
    @PostMapping("/recuperar-senha")
    @RateLimiter(name = "recuperacao", fallbackMethod = "recuperacaoBloqueada")
    public ResponseEntity<String> recuperarSenha(@RequestBody RecuperarSenhaRequest dto) {
        useCase.enviarCodigo(dto.email());
        return ResponseEntity.ok("Código enviado para o e-mail informado.");
    }

    @Operation(summary = "Valida o código enviado ao usuário")
    @PostMapping("/validar-codigo")
    @RateLimiter(name = "validacao", fallbackMethod = "validacaoBloqueada")
    public ResponseEntity<String> validarCodigo(@RequestBody ValidarCodigoRequest dto) {
        useCase.validarCodigo(dto.email(), dto.codigo());
        return ResponseEntity.ok("Código válido.");
    }

    @Operation(summary = "Reseta a senha do usuário")
    @PostMapping("/resetar-senha")
    public ResponseEntity<String> resetarSenha(@RequestBody ResetarSenhaRequest dto) {
        useCase.resetarSenha(dto.email(), dto.novaSenha());
        return ResponseEntity.ok("Senha alterada com sucesso!");
    }

    // Métodos fallback para Rate Limiting
    private ResponseEntity<String> recuperacaoBloqueada(RecuperarSenhaRequest dto, RequestNotPermitted ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Muitas tentativas de recuperação. Aguarde 1 hora.");
    }

    private ResponseEntity<String> validacaoBloqueada(ValidarCodigoRequest dto, RequestNotPermitted ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Muitas tentativas de validação. Aguarde 5 minutos.");
    }
}

