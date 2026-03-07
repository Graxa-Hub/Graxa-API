package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.CredenciaisUsuarioService;
import com.Graxa_API.Graxa_API.dto.credencialUsuarioDto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credenciais")
@Tag(name = "Credenciais", description = "Endpoints relacionados às credenciais de acesso dos usuários")
public class CredenciaisUsuarioController {

    private final CredenciaisUsuarioService service;

    public CredenciaisUsuarioController(CredenciaisUsuarioService service) {
        this.service = service;
    }

    @Operation(summary = "Realiza login com credenciais do usuário")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestLoginDto dto) {
        return service.login(dto.identificador(), dto.senha());
    }

    @Operation(summary = "Busca credencial de usuário pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<?> getCredencialPorId(@PathVariable Long id) {
        return service.getCredencial(id);
    }

    @Operation(summary = "Atualiza credencial de usuário", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCredencial(@PathVariable Long id, @Valid @RequestBody RequestCredenciaisUsuarioDto dto) {
        return service.updateCredencial(id, dto);
    }

    @Operation(summary = "Deleta credencial de usuário pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarCredencial(@PathVariable Long id) {
        return service.deletarCredencial(id);
    }
}
