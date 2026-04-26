package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.ShowService;
import com.Graxa_API.Graxa_API.dto.ShowDto.RequestBandasShowDto;
import com.Graxa_API.Graxa_API.dto.ShowDto.RequestShowDto;
import com.Graxa_API.Graxa_API.dto.ShowDto.ResponseShowDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shows")
@Tag(name = "Show", description = "Endpoints relacionados à gestão de shows")
public class ShowController {

    private final ShowService service;

    public ShowController(ShowService service) {
        this.service = service;
    }

    @Operation(summary = "Cria um novo show", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    @PreAuthorize("hasRole('PRODUCAO')")
    public ResponseEntity<ResponseShowDto> criar(@RequestBody @Valid RequestShowDto dto) {
        return service.criar(dto);
    }

    @Operation(summary = "Busca um show pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")   // verificação de alocação feita no service
    public ResponseEntity<ResponseShowDto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @Operation(summary = "Lista todos os shows", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    @PreAuthorize("isAuthenticated()")   // verificação de alocação feita no service
    public ResponseEntity<List<ResponseShowDto>> listarTodos() {
        return service.listarTodos();
    }

    @Operation(summary = "Atualiza um show existente", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PRODUCAO')")
    public ResponseEntity<ResponseShowDto> atualizar(@PathVariable Long id, @RequestBody @Valid RequestShowDto dto) {
        return service.atualizar(id, dto);
    }

    @Operation(summary = "Deleta um show pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PRODUCAO')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return service.deletar(id);
    }

    @Operation(summary = "Adiciona bandas a um show existente", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/bandas")
    @PreAuthorize("hasRole('PRODUCAO')")
    public ResponseEntity<ResponseShowDto> adicionarBandas(@RequestBody @Valid RequestBandasShowDto dto) {
        return service.adicionarBandasAoShow(dto);
    }
}