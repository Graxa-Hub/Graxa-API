package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.BandaService;
import com.Graxa_API.Graxa_API.dto.BandaDto.RequestBandaDto;
import com.Graxa_API.Graxa_API.dto.BandaDto.RequestIntegrantesDto;
import com.Graxa_API.Graxa_API.dto.BandaDto.ResponseBandaDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bandas")
@Tag(name = "Banda", description = "Endpoints relacionados à gestão de bandas e seus integrantes")
public class BandaController {

    private final BandaService service;

    public BandaController(BandaService service) {
        this.service = service;
    }

    @Operation(summary = "Lista todas as bandas", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    public ResponseEntity<List<ResponseBandaDto>> getbandas() {
        return service.getBandas();
    }

    @Operation(summary = "Cria uma nova banda", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    public ResponseEntity<ResponseBandaDto> criarBanda(@RequestBody RequestBandaDto banda) {
        return service.criarBanda(banda);
    }

    @Operation(summary = "Adiciona integrantes a uma banda existente", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping("/{id}/integrantes")
    public ResponseEntity<List<ResponseBandaDto>> adicionarIntegrantes(
            @PathVariable Long id,
            @RequestBody List<RequestIntegrantesDto> integrantes) {
        List<ResponseBandaDto> integrantesBanda = integrantes.stream()
                .map(integrante -> service.adicionarIntegranteBanda(id, integrante).getBody())
                .toList();

        return ResponseEntity.ok(integrantesBanda);
    }
}
