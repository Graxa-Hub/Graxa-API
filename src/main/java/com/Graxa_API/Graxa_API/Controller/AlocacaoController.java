package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Enums.StatusAlocacao;
import com.Graxa_API.Graxa_API.Service.AlocacaoService;
import com.Graxa_API.Graxa_API.dto.AlocacaoDto.RequestAlocacaoDto;
import com.Graxa_API.Graxa_API.dto.AlocacaoDto.ResponseAlocacaoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alocacoes")
public class AlocacaoController {

    private final AlocacaoService alocacaoService;

    public AlocacaoController(AlocacaoService alocacaoService) {
        this.alocacaoService = alocacaoService;
    }

    // Só PRODUCAO pode criar alocação — ownership verificada no service
    @PostMapping
    @PreAuthorize("hasRole('PRODUCAO')")
    public ResponseEntity<ResponseAlocacaoDto> criarAlocacao(@RequestBody RequestAlocacaoDto dto) {
        ResponseAlocacaoDto response = alocacaoService.criarAlocacao(dto);
        return ResponseEntity.status(201).body(response);
    }

    // Qualquer autenticado pode tentar — service valida se é o destinatário
    @PutMapping("/{id}/responder")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseAlocacaoDto> responderAlocacao(
            @PathVariable Long id,
            @RequestParam StatusAlocacao status
    ) {
        ResponseAlocacaoDto response = alocacaoService.responderAlocacao(id, status);
        return ResponseEntity.ok(response);
    }

    // PRODUCAO vê todas as alocações do show, outros tipos veem só as suas — lógica no service
    @GetMapping("/show/{showId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ResponseAlocacaoDto>> listarPorShow(@PathVariable Long showId) {
        List<ResponseAlocacaoDto> response = alocacaoService.listarPorShow(showId);
        return ResponseEntity.ok(response);
    }
}