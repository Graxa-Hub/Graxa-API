package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.CredenciaisUsuarioService;
import com.Graxa_API.Graxa_API.dto.credencialUsuarioDto.RequestCredenciaisUsuarioDto;
import com.Graxa_API.Graxa_API.dto.credencialUsuarioDto.RequestLoginDto;
import com.Graxa_API.Graxa_API.dto.credencialUsuarioDto.ResponseCredenciaisUsuarioDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credenciais")
public class CredenciaisUsuarioController {

    private final CredenciaisUsuarioService service;

    public CredenciaisUsuarioController(CredenciaisUsuarioService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCredencialPorId(@PathVariable Long id) {
        return service.getCredencial(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCredencial(@PathVariable Long id, @Valid @RequestBody RequestCredenciaisUsuarioDto dto) {
        return service.updateCredencial(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarCredencial(@PathVariable Long id) {
        return service.deletarCredencial(id);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestLoginDto dto) {
        return service.login(dto.identificador(), dto.senha());
    }

}
