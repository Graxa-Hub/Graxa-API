package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.ShowService;
import com.Graxa_API.Graxa_API.dto.ShowDto.RequestBandasShowDto;
import com.Graxa_API.Graxa_API.dto.ShowDto.RequestShowDto;
import com.Graxa_API.Graxa_API.dto.ShowDto.ResponseShowDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shows")
public class ShowController {

    private final ShowService service;

    public ShowController(ShowService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ResponseShowDto> criar(@RequestBody @Valid RequestShowDto dto) {
        return service.criar(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseShowDto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping
    public ResponseEntity<List<ResponseShowDto>> listarTodos() {
        return service.listarTodos();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseShowDto> atualizar(@PathVariable Long id, @RequestBody @Valid RequestShowDto dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return service.deletar(id);
    }

    @PutMapping("/bandas")
    public ResponseEntity<ResponseShowDto> adicionarBandas(@RequestBody @Valid RequestBandasShowDto dto) {
        return service.adicionarBandasAoShow(dto);
    }
}
