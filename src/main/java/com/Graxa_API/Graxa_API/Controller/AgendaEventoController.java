package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Entity.Evento.AgendaEventoEntity;
import com.Graxa_API.Graxa_API.Service.AgendaEventoService;
import com.Graxa_API.Graxa_API.dto.Agenda.AgendaEventoCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agenda-evento")
@CrossOrigin("*")
public class AgendaEventoController {

    @Autowired
    private AgendaEventoService agendaEventoService;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody AgendaEventoCreateDTO dto) {
        AgendaEventoEntity saved = agendaEventoService.criar(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/show/{showId}")
    public ResponseEntity<List<AgendaEventoEntity>> listar(@PathVariable Long showId) {
        return ResponseEntity.ok(agendaEventoService.listarPorShow(showId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {
        agendaEventoService.remover(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendaEventoEntity> atualizar(
            @PathVariable Long id,
            @RequestBody AgendaEventoEntity dto) {

        return ResponseEntity.ok(agendaEventoService.atualizar(id, dto));
    }
}
