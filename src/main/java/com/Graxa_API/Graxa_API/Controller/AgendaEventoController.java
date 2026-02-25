package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Entity.Evento.AgendaEventoEntity;
import com.Graxa_API.Graxa_API.Service.AgendaEventoService;
import com.Graxa_API.Graxa_API.dto.Agenda.AgendaEventoCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agenda-evento")
@CrossOrigin("*")
public class AgendaEventoController {

    @Autowired
    private AgendaEventoService agendaEventoService;

    @PostMapping
    @PreAuthorize("hasRole('PRODUTOR')")
    public ResponseEntity<AgendaEventoEntity> criar(@RequestBody AgendaEventoCreateDTO dto) {
        AgendaEventoEntity saved = agendaEventoService.criar(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/show/{showId}")
    public ResponseEntity<List<AgendaEventoEntity>> listar(@PathVariable Long showId) {
        return ResponseEntity.ok(agendaEventoService.listarPorShow(showId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PRODUTOR')")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        agendaEventoService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PRODUTOR')")
    public ResponseEntity<AgendaEventoEntity> atualizar(
            @PathVariable Long id,
            @RequestBody AgendaEventoCreateDTO dto) {

        AgendaEventoEntity updated = agendaEventoService.atualizar(id, dto);
        return ResponseEntity.ok(updated);
    }
}
