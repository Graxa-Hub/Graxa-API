package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.BandaService;
import com.Graxa_API.Graxa_API.dto.BandaDto.RequestBandaDto;
import com.Graxa_API.Graxa_API.dto.BandaDto.RequestIntegrantesDto;
import com.Graxa_API.Graxa_API.dto.BandaDto.ResponseBandaDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/bandas")
@PreAuthorize("hasRole('PRODUCAO')")
@Tag(name = "Bandas", description = "Endpoints para gerenciamento de bandas")
public class BandaController {

    private final BandaService service;

    public BandaController(BandaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todas as bandas")
    public ResponseEntity<Page<ResponseBandaDto>> getBandas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<ResponseBandaDto> bandas = service.getBandas(pageable);
        return bandas.isEmpty()
                ? ResponseEntity.ok(Page.empty(pageable))
                : ResponseEntity.ok(bandas);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar bandas por nome")
    public ResponseEntity<List<ResponseBandaDto>> getBandasPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(service.getBandasPorNome(nome));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar banda por ID")
    public ResponseEntity<ResponseBandaDto> getBandaPorId(@PathVariable Long id) {
        long inicio = System.currentTimeMillis();
        ResponseBandaDto banda = service.getBandaPorId(id);
        long fim = System.currentTimeMillis();
        System.out.println("GET /bandas/" + id + " demorou " + (fim - inicio) + "ms");
        return ResponseEntity.ok(banda);
    }

    @PostMapping
    @Operation(summary = "Criar nova banda")
    public ResponseEntity<ResponseBandaDto> criarBanda(
            @RequestPart("dados") @Valid RequestBandaDto dto,
            @RequestPart(value = "foto", required = false) MultipartFile foto
    ) throws IOException {
        return ResponseEntity.status(201).body(service.criarBanda(dto, foto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar banda")
    public ResponseEntity<ResponseBandaDto> atualizarBanda(
            @PathVariable Long id,
            @RequestPart("dados") @Valid RequestBandaDto dto,
            @RequestPart(value = "foto", required = false) MultipartFile foto
    ) throws IOException {
        return ResponseEntity.ok(service.atualizarBanda(id, dto, foto));
    }

    @PostMapping("/{id}/integrantes")
    @Operation(summary = "Adicionar integrantes à banda")
    public ResponseEntity<ResponseBandaDto> adicionarIntegrante(
            @PathVariable Long id,
            @RequestBody @Valid RequestIntegrantesDto dto
    ) {
        return ResponseEntity.ok(service.adicionarIntegranteBanda(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Realizar um safe delete à banda")
    public ResponseEntity<Void> deletarBanda(@PathVariable Long id) {
        service.deletarBanda(id);
        return ResponseEntity.noContent().build();
    }
}
