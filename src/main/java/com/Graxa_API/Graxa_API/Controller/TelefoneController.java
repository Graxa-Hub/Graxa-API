package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.TelefoneService;
import com.Graxa_API.Graxa_API.dto.TelefoneDto.ResponseTelefoneDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/telefones")
@Tag(name = "Telefone", description = "Endpoints relacionados aos telefones dos usuários")
public class TelefoneController {

    private final TelefoneService service;

    public TelefoneController(TelefoneService service) {
        this.service = service;
    }

    @Operation(summary = "Lista os telefones vinculados a um usuário pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<List<ResponseTelefoneDto>> listarTelefonesPorUsuairo(@PathVariable Long id) {
        return service.listarPorUsuario(id);
    }
}
