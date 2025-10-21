package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Repository.TelefoneRepository;
import com.Graxa_API.Graxa_API.Service.TelefoneService;
import com.Graxa_API.Graxa_API.dto.TelefoneDto.ResponseTelefoneDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/telefones")
public class TelefoneController {
    TelefoneService service;

    public TelefoneController(TelefoneService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ResponseTelefoneDto>> listarTelefonesPorUsuairo(@PathVariable Long id){
        return service.listarPorUsuario(id);

    }
}
