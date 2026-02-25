package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.ArtistaService;
import com.Graxa_API.Graxa_API.dto.ArtistaDto.RequestArtistaDto;
import com.Graxa_API.Graxa_API.dto.ArtistaDto.ResponseArtistaDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artistas")
@Tag(name = "Artista", description = "Endpoints relacionados à gestão de artistas")
public class ArtistaController {

    private final ArtistaService service;

    public ArtistaController(ArtistaService service) {
        this.service = service;
    }

    @Operation(summary = "Lista todos os artistas", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    public ResponseEntity<List<ResponseArtistaDto>> listarTodos() {
        return service.listarArtistas();
    }

    @Operation(summary = "Busca um artista pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<ResponseArtistaDto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @Operation(summary = "Cria um novo artista", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    @PreAuthorize("hasRole('PRODUTOR')")
    public ResponseEntity<ResponseArtistaDto> criar(@RequestBody @Valid RequestArtistaDto dto) {
        return service.criarArtista(dto);
    }

    @Operation(summary = "Atualiza os dados de um artista", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PRODUTOR')")
    public ResponseEntity<ResponseArtistaDto> atualizar(@PathVariable Long id, @RequestBody @Valid RequestArtistaDto dto) {
        return service.atualizarArtista(id, dto);
    }

    @Operation(summary = "Deleta um artista pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PRODUTOR')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return service.deletarArtista(id);
    }
}
