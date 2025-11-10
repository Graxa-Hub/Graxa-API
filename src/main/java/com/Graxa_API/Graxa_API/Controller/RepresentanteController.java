package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.RepresentanteService;
import com.Graxa_API.Graxa_API.dto.RepresentanteDto.RequestRepresentanteDto;
import com.Graxa_API.Graxa_API.dto.RepresentanteDto.ResponseRepresentanteDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/representantes")
@Tag(name = "Representantes", description = "Endpoints para gerenciamento de representantes")
public class RepresentanteController {

    private final RepresentanteService service;

    public RepresentanteController(RepresentanteService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos os representantes")
    public ResponseEntity<List<ResponseRepresentanteDto>> listarRepresentantes() {
        return service.listarRepresentantes();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar representante por ID")
    public ResponseEntity<ResponseRepresentanteDto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar representante por email")
    public ResponseEntity<ResponseRepresentanteDto> buscarPorEmail(@PathVariable String email) {
        return service.buscarPorEmail(email);
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar representante por nome")
    public ResponseEntity<ResponseRepresentanteDto> buscarPorNome(@PathVariable String nome) {
        return service.buscarPorNome(nome);
    }

    @PostMapping
    @Operation(summary = "Criar novo representante")
    public ResponseEntity<ResponseRepresentanteDto> criarRepresentante(@RequestBody @Valid RequestRepresentanteDto dto) {
        return service.criarRepresentante(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar representante")
    public ResponseEntity<ResponseRepresentanteDto> atualizarRepresentante(
            @PathVariable Long id,
            @RequestBody @Valid RequestRepresentanteDto dto
    ) {
        return service.atualizarRepresentante(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar representante")
    public ResponseEntity<Void> deletarRepresentante(@PathVariable Long id) {
        return service.deletarRepresentante(id);
    }
}