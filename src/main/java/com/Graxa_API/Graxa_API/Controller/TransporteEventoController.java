package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Entity.Evento.TransporteEventoEntity;
import com.Graxa_API.Graxa_API.Service.TransporteEventoService;
import com.Graxa_API.Graxa_API.dto.Transporte.TransporteEventoCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transporte-evento")
@CrossOrigin("*")
public class TransporteEventoController {

    @Autowired
    private TransporteEventoService transporteEventoService;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody TransporteEventoCreateDTO dto) {
        TransporteEventoEntity saved = transporteEventoService.criar(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/show/{showId}")
    public ResponseEntity<List<TransporteEventoEntity>> listar(@PathVariable Long showId) {
        return ResponseEntity.ok(transporteEventoService.listarPorShow(showId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {
        transporteEventoService.remover(id);
        return ResponseEntity.ok().build();
    }
}
