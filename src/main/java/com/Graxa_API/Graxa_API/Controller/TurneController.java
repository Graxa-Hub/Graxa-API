package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.TurneService;
import com.Graxa_API.Graxa_API.dto.TurneDto.RequestTurneDto;
import com.Graxa_API.Graxa_API.dto.TurneDto.ResponseTurneDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turne")
@Tag(name = "Turnê", description = "Endpoints relacionados à gestão de turnês")
public class TurneController {

    private final TurneService service;

    public TurneController(TurneService service) {
        this.service = service;
    }

    @Operation(summary = "Cria uma nova turnê", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    public ResponseEntity<ResponseTurneDto> criar(@Valid @RequestBody RequestTurneDto dto) {
        return service.criarTurne(dto);
    }

    @Operation(summary = "Busca uma turnê pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<ResponseTurneDto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @Operation(summary = "Busca uma turnê pelo nome", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/nome/{nome}")
    public ResponseEntity<ResponseTurneDto> buscarPorNome(@PathVariable String nome) {
        return service.buscarPorNome(nome);
    }

    @Operation(summary = "Atualiza uma turnê existente", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<ResponseTurneDto> atualizar(@PathVariable Long id, @Valid @RequestBody RequestTurneDto dto) {
        return service.atualizarTurne(id, dto);
    }

    @Operation(summary = "Deleta uma turnê pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return service.deletarTurne(id);
    }

    @Operation(summary = "Lista todas as turnês ativas", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    public ResponseEntity<List<ResponseTurneDto>> listarAtivas() {
        return service.listarAtivas();
    }
}
