package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.TurneService;
import com.Graxa_API.Graxa_API.dto.TurneDto.RequestTurneDto;
import com.Graxa_API.Graxa_API.dto.TurneDto.ResponseTurneDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/turne")
public class TurneController {
    private final TurneService service;

    public TurneController(TurneService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ResponseTurneDto> criar(@Valid @RequestBody RequestTurneDto dto){
        return service.criarTurne(dto);
    }
}
