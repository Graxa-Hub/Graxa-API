package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.TurneService;
import com.Graxa_API.Graxa_API.dto.TurneDto.RequestTurneDto;
import com.Graxa_API.Graxa_API.dto.TurneDto.ResponseTurneDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turne")
public class TurneController {
    private final TurneService service;

    public TurneController(TurneService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ResponseTurneDto> criar(@Valid @RequestBody RequestTurneDto dto) {
        return service.criarTurne(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseTurneDto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<ResponseTurneDto> buscarPorNome(@PathVariable String nome) {
        return service.buscarPorNome(nome);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseTurneDto> atualizar(@PathVariable Long id, @Valid @RequestBody RequestTurneDto dto) {
        return service.atualizarTurne(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return service.deletarTurne(id);
    }

    @GetMapping
    public ResponseEntity<List<ResponseTurneDto>> listarAtivas() {
        return service.listarAtivas();

    }
}
