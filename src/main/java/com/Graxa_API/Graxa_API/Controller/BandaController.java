package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.BandaService;
import com.Graxa_API.Graxa_API.dto.BandaDto.RequestBandaDto;
import com.Graxa_API.Graxa_API.dto.BandaDto.RequestIntegrantesDto;
import com.Graxa_API.Graxa_API.dto.BandaDto.ResponseBandaDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bandas")
public class BandaController {
    private final BandaService service;

    public BandaController(BandaService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<List<ResponseBandaDto>> getbandas(){
        return service.getBandas();
    }
    @PostMapping()
    public ResponseEntity<ResponseBandaDto> criarBanda(@RequestBody RequestBandaDto banda){
        return service.criarBanda(banda);
    }

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
