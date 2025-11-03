package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.LocalService;
import com.Graxa_API.Graxa_API.dto.LocalDto.RequestLocalDto;
import com.Graxa_API.Graxa_API.dto.LocalDto.ResponseLocalDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locais")
@Tag(name = "Local", description = "Endpoints relacionados à gestão de locais")
public class LocalController {

    private final LocalService service;

    public LocalController(LocalService service) {
        this.service = service;
    }

    @Operation(summary = "Cria um novo local", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    public ResponseEntity<ResponseLocalDto> criar(@Valid @RequestBody RequestLocalDto dto) {
        return service.criarLocal(dto);
    }

    @Operation(summary = "Busca um local pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<ResponseLocalDto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @Operation(summary = "Atualiza um local existente", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<ResponseLocalDto> atualizar(@PathVariable Long id, @Valid @RequestBody RequestLocalDto dto) {
        return service.atualizarLocal(id, dto);
    }

    @Operation(summary = "Deleta um local pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return service.deletarLocal(id);
    }

    @Operation(summary = "Lista todos os locais", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    public ResponseEntity<List<ResponseLocalDto>> listarTodos() {
        return service.listarTodos();
    }
}
