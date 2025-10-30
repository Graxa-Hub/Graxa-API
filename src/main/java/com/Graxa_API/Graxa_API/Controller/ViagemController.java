package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.ViagemService;
import com.Graxa_API.Graxa_API.dto.ViagemDto.RequestViagemDto;
import com.Graxa_API.Graxa_API.dto.ViagemDto.ResponseViagemDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/viagem")
public class ViagemController {

    private final ViagemService viagemService;

    public ViagemController(ViagemService viagemService) {
        this.viagemService = viagemService;
    }

    @PostMapping
    public ResponseEntity<ResponseViagemDto> criar(@RequestBody @Valid RequestViagemDto dto) {
        return viagemService.criar(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseViagemDto> buscarPorId(@PathVariable Long id) {
        return viagemService.buscarPorId(id);
    }

    @GetMapping
    public ResponseEntity<List<ResponseViagemDto>> listarTodos() {
        return viagemService.listarTodos();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ResponseViagemDto>> buscarPorNome(@RequestParam String nome) {
        return viagemService.buscarPorNome(nome);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseViagemDto> atualizar(@PathVariable Long id, @RequestBody @Valid RequestViagemDto dto) {
        return viagemService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return viagemService.deletar(id);
    }
}
