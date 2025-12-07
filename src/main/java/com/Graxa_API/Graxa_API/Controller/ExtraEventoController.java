package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.ExtraEventoService;
import com.Graxa_API.Graxa_API.dto.Extra.ExtraEventoCreateDTO;
import com.Graxa_API.Graxa_API.dto.Logistica.ExtraDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/extra-evento")
@CrossOrigin("*")
public class ExtraEventoController {

    @Autowired
    private ExtraEventoService service;

    @GetMapping("/show/{showId}")
    public ResponseEntity<ExtraDTO> buscar(@PathVariable Long showId) {
        return ResponseEntity.ok(service.buscarPorShow(showId));
    }

    @PostMapping
    public ResponseEntity<ExtraDTO> salvar(@RequestBody ExtraEventoCreateDTO dto) {
        return ResponseEntity.ok(service.criarOuAtualizar(dto));
    }

    // opcional: PUT também pode existir
    @PutMapping
    public ResponseEntity<ExtraDTO> update(@RequestBody ExtraEventoCreateDTO dto) {
        return ResponseEntity.ok(service.criarOuAtualizar(dto));
    }
}
