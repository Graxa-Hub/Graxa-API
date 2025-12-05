package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.Repository.ShowRepository;
import com.Graxa_API.Graxa_API.Service.VisaoEventoService;
import com.Graxa_API.Graxa_API.dto.VisaoEventoDto.VisaoEventoDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/visao-evento")
@Tag(
        name = "Visão do Evento",
        description = "Endpoint especializado em fornecer dados completos e formatados para a tela Visão de Evento"
)
public class VisaoEventoController {

    private final ShowRepository showRepository;
    private final VisaoEventoService visaoEventoService;

    public VisaoEventoController(
            ShowRepository showRepository,
            VisaoEventoService visaoEventoService
    ) {
        this.showRepository = showRepository;
        this.visaoEventoService = visaoEventoService;
    }

    @Operation(
            summary = "Retorna a visão completa do show",
            description = "Retorna artista principal, turnê, data formatada, cidade, progresso e agenda customizada.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @GetMapping("/show/{id}")
    public ResponseEntity<VisaoEventoDto> visaoDoShow(@PathVariable Long id) {

        ShowEntity show = showRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Show não encontrado"));

        VisaoEventoDto dto = visaoEventoService.montarVisaoEvento(show);

        return ResponseEntity.ok(dto);
    }
}
