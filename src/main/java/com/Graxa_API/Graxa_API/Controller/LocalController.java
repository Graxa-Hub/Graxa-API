package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.LocalService;
import com.Graxa_API.Graxa_API.dto.LocalDto.RequestLocalDto;
import com.Graxa_API.Graxa_API.dto.LocalDto.ResponseLocalDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locais")
public class LocalController {

    private final LocalService service;

    public LocalController(LocalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ResponseLocalDto> criar(@Valid @RequestBody RequestLocalDto dto) {
        return service.criarLocal(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseLocalDto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseLocalDto> atualizar(@PathVariable Long id, @Valid @RequestBody RequestLocalDto dto) {
        return service.atualizarLocal(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return service.deletarLocal(id);
    }

    @GetMapping
    public ResponseEntity<List<ResponseLocalDto>> listarTodos() {
        return service.listarTodos();
    }
}
