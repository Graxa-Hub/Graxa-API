package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.AlocacaoService;
import com.Graxa_API.Graxa_API.dto.AlocacaoDto.RequestAlocacaoDto;
import com.Graxa_API.Graxa_API.dto.AlocacaoDto.ResponseAlocacaoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alocacoes")
public class AlocacaoController {

    private final AlocacaoService alocacaoService;

    public AlocacaoController(AlocacaoService alocacaoService) {
        this.alocacaoService = alocacaoService;
    }

    @PostMapping
    public ResponseEntity<ResponseAlocacaoDto> criarAlocacao(@RequestBody RequestAlocacaoDto dto) {
        ResponseAlocacaoDto response = alocacaoService.criarAlocacao(dto);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}/responder")
    public ResponseEntity<ResponseAlocacaoDto> responderAlocacao(
            @PathVariable Long id,
            @RequestParam boolean aceita
    ) {
        ResponseAlocacaoDto response = alocacaoService.responderAlocacao(id, aceita);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/show/{showId}")
    public ResponseEntity<List<ResponseAlocacaoDto>> listarPorShow(@PathVariable Long showId) {
        List<ResponseAlocacaoDto> response = alocacaoService.listarPorShow(showId);
        return ResponseEntity.ok(response);
    }
}
