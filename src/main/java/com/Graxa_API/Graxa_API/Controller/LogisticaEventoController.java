package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.LogisticaEventoService;
import com.Graxa_API.Graxa_API.dto.Logistica.LogisticaEventoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logistica")
@CrossOrigin("*")
public class LogisticaEventoController {

    @Autowired
    private LogisticaEventoService service;

    @GetMapping("/show/{showId}")
    public ResponseEntity<LogisticaEventoResponseDTO> buscar(@PathVariable Long showId) {
        return ResponseEntity.ok(service.buscarPorShow(showId));
    }
}
