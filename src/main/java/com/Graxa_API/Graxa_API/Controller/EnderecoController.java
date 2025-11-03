package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.EnderecoService;
import com.Graxa_API.Graxa_API.dto.EnderecoDto.RequestEnderecoDto;
import com.Graxa_API.Graxa_API.dto.EnderecoDto.ResponseEnderecoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
@Tag(name = "Endereço", description = "Endpoints relacionados à gestão de endereços")
public class EnderecoController {

    private final EnderecoService service;

    public EnderecoController(EnderecoService service) {
        this.service = service;
    }

    @Operation(summary = "Cria um novo endereço", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    public ResponseEntity<ResponseEnderecoDto> criar(@Valid @RequestBody RequestEnderecoDto dto) {
        return service.criarEndereco(dto);
    }

    @Operation(summary = "Busca um endereço pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<ResponseEnderecoDto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @Operation(summary = "Atualiza um endereço existente", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<ResponseEnderecoDto> atualizar(@PathVariable Long id, @Valid @RequestBody RequestEnderecoDto dto) {
        return service.atualizarEndereco(id, dto);
    }

    @Operation(summary = "Deleta um endereço pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return service.deletarEndereco(id);
    }

    @Operation(summary = "Lista todos os endereços", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    public ResponseEntity<List<ResponseEnderecoDto>> listarTodos() {
        return service.listarTodos();
    }
}
