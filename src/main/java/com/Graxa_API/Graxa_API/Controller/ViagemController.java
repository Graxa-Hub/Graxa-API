package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.ViagemService;
import com.Graxa_API.Graxa_API.dto.ViagemDto.RequestViagemDto;
import com.Graxa_API.Graxa_API.dto.ViagemDto.ResponseViagemDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/viagem")
@Tag(name = "Viagem", description = "Endpoints relacionados à gestão de viagens")
public class ViagemController {

    private final ViagemService viagemService;

    public ViagemController(ViagemService viagemService) {
        this.viagemService = viagemService;
    }

    @Operation(summary = "Cria uma nova viagem", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    public ResponseEntity<ResponseViagemDto> criar(@RequestBody @Valid RequestViagemDto dto) {
        return viagemService.criar(dto);
    }

    @Operation(summary = "Busca uma viagem pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<ResponseViagemDto> buscarPorId(@PathVariable Long id) {
        return viagemService.buscarPorId(id);
    }

    @Operation(summary = "Lista todas as viagens", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    public ResponseEntity<List<ResponseViagemDto>> listarTodos() {
        return viagemService.listarTodos();
    }

    @Operation(summary = "Busca viagens pelo nome", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/buscar")
    public ResponseEntity<List<ResponseViagemDto>> buscarPorNome(@RequestParam String nome) {
        return viagemService.buscarPorNome(nome);
    }

    @Operation(summary = "Atualiza uma viagem existente", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<ResponseViagemDto> atualizar(@PathVariable Long id, @RequestBody @Valid RequestViagemDto dto) {
        return viagemService.atualizar(id, dto);
    }

    @Operation(summary = "Deleta uma viagem pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return viagemService.deletar(id);
    }
}
