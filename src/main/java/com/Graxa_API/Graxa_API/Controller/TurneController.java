package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.TurneService;
import com.Graxa_API.Graxa_API.dto.TurneDto.RequestTurneDto;
import com.Graxa_API.Graxa_API.dto.TurneDto.ResponseTurneDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/turnes")
@Tag(name = "Turnê", description = "Endpoints relacionados à gestão de turnês")
public class TurneController {

    private final TurneService turneService;

    public TurneController(TurneService turneService) {
        this.turneService = turneService;
    }

    @Operation(summary = "Cria uma nova turnê", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    @PreAuthorize("hasRole('PRODUTOR')")
    public ResponseEntity<ResponseTurneDto> criarTurne(
            @RequestPart("dados") @Valid RequestTurneDto dto,
            @RequestPart("imagem") MultipartFile imagem
    ) throws IOException {
        // ✅ delega direto para o service
        return turneService.criarTurne(dto, imagem);
    }

    @Operation(summary = "Busca uma turnê pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<ResponseTurneDto> buscarPorId(@PathVariable Long id) {
        return turneService.buscarPorId(id);
    }

    @Operation(summary = "Busca uma turnê pelo nome", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/nome/{nome}")
    public ResponseEntity<ResponseTurneDto> buscarPorNome(@PathVariable String nome) {
        return turneService.buscarPorNome(nome);
    }

    @Operation(summary = "Atualiza uma turnê existente", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PRODUTOR')")
    public ResponseEntity<ResponseTurneDto> atualizar(
            @PathVariable Long id,
            @RequestPart("dados") @Valid RequestTurneDto dto,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem
    ) throws IOException {
        // ✅ delega direto para o service
        return turneService.atualizarTurne(id, dto, imagem);
    }

    @Operation(summary = "Deleta uma turnê pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PRODUTOR')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return turneService.deletarTurne(id);
    }

    @Operation(summary = "Lista todas as turnês ativas", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    public ResponseEntity<List<ResponseTurneDto>> listarAtivas() {
        return turneService.listarAtivas();
    }
}
