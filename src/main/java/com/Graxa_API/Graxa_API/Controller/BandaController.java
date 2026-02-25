package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.BandaService;
import com.Graxa_API.Graxa_API.dto.BandaDto.RequestBandaDto;
import com.Graxa_API.Graxa_API.dto.BandaDto.RequestIntegrantesDto;
import com.Graxa_API.Graxa_API.dto.BandaDto.ResponseBandaDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/bandas")
@Tag(name = "Bandas", description = "Endpoints para gerenciamento de bandas")
@PreAuthorize("hasRole('PRODUTOR')")
public class BandaController {

    private final BandaService service;

    public BandaController(BandaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todas as bandas")
    public ResponseEntity<List<ResponseBandaDto>> getBandas() {
        return service.getBandas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar banda por ID")
    public ResponseEntity<ResponseBandaDto> getBandaPorId(@PathVariable Long id) {
        return service.getBandaPorId(id);
    }

    @PostMapping
    @Operation(summary = "Criar nova banda")
    public ResponseEntity<ResponseBandaDto> criarBanda(
            @RequestPart("dados") @Valid RequestBandaDto dto,
            @RequestPart(value = "foto", required = false) MultipartFile foto
    ) throws IOException {
        return service.criarBanda(dto, foto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar banda")
    public ResponseEntity<ResponseBandaDto> atualizarBanda(
            @PathVariable Long id,
            @RequestPart("dados") @Valid RequestBandaDto dto,
            @RequestPart(value = "foto", required = false) MultipartFile foto
    ) throws IOException {
        return service.atualizarBanda(id, dto, foto);
    }

    @PostMapping("/{id}/integrantes")
    @Operation(summary = "Adicionar integrantes à banda")
    public ResponseEntity<ResponseBandaDto> adicionarIntegrante(
            @PathVariable Long id,
            @RequestBody @Valid RequestIntegrantesDto dto
    ) {
        return service.adicionarIntegranteBanda(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Realizer um safe delete á banda")
    public ResponseEntity<Void> deletarBanda(@PathVariable Long id){
        service.deletarBanda(id);
        return ResponseEntity.noContent().build();
    }
}