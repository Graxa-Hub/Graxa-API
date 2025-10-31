package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.ArtistaService;
import com.Graxa_API.Graxa_API.dto.ArtistaDto.RequestArtistaDto;
import com.Graxa_API.Graxa_API.dto.ArtistaDto.ResponseArtistaDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artistas")
public class ArtistaController {

    private final ArtistaService service;

    public ArtistaController(ArtistaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ResponseArtistaDto>> listarTodos() {
        return service.listarArtistas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseArtistaDto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ResponseArtistaDto> criar(@RequestBody @Valid RequestArtistaDto dto) {
        return service.criarArtista(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseArtistaDto> atualizar(@PathVariable Long id, @RequestBody @Valid RequestArtistaDto dto) {
        return service.atualizarArtista(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return service.deletarArtista(id);
    }
}
