package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Entity.Evento.VooEventoEntity;
import com.Graxa_API.Graxa_API.Service.VooEventoService;
import com.Graxa_API.Graxa_API.dto.Voo.VooEventoCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voo-evento")
@CrossOrigin("*")
public class VooEventoController {

    @Autowired
    private VooEventoService vooEventoService;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody VooEventoCreateDTO dto) {
        VooEventoEntity saved = vooEventoService.criar(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/show/{showId}")
    public ResponseEntity<List<VooEventoEntity>> listar(@PathVariable Long showId) {
        return ResponseEntity.ok(vooEventoService.listarPorShow(showId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {
        vooEventoService.remover(id);
        return ResponseEntity.ok().build();
    }
}
