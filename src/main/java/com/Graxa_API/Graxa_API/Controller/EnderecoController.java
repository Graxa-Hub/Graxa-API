package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.EnderecoService;
import com.Graxa_API.Graxa_API.dto.EnderecoDto.RequestEnderecoDto;
import com.Graxa_API.Graxa_API.dto.EnderecoDto.ResponseEnderecoDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    private final EnderecoService service;

    public EnderecoController(EnderecoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ResponseEnderecoDto> criar(@Valid @RequestBody RequestEnderecoDto dto) {
        return service.criarEndereco(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseEnderecoDto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseEnderecoDto> atualizar(@PathVariable Long id, @Valid @RequestBody RequestEnderecoDto dto) {
        return service.atualizarEndereco(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return service.deletarEndereco(id);
    }

    @GetMapping
    public ResponseEntity<List<ResponseEnderecoDto>> listarTodos() {
        return service.listarTodos();
    }
}
