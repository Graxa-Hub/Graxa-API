package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.TelefoneService;
import com.Graxa_API.Graxa_API.dto.TelefoneDto.RequestTelefoneDto;
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

    @Operation(
            summary = "Lista todos os telefones de um usuário",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<ResponseTelefoneDto>> listarTelefonesPorUsuario(
            @PathVariable Long usuarioId
    ) {
        return service.listarPorUsuario(usuarioId);
    }

    @Operation(
            summary = "Busca telefone pelo ID",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @GetMapping("/telefone/{id}")
    public ResponseEntity<ResponseTelefoneDto> buscarTelefonePorId(
            @PathVariable Long id
    ) {
        return service.buscarPorId(id);
    }

    @Operation(
            summary = "Cria telefone para usuário",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @PostMapping("/{usuarioId}")
    public ResponseEntity<ResponseTelefoneDto> criarTelefoneParaUsuario(
            @PathVariable Long usuarioId,
            @RequestBody RequestTelefoneDto dto
    ) {
        return service.criarTelefoneParaUsuario(usuarioId, dto);
    }

    @Operation(
            summary = "Atualiza telefone",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @PutMapping("/telefone/{id}")
    public ResponseEntity<ResponseTelefoneDto> atualizarTelefone(
            @PathVariable Long id,
            @RequestBody RequestTelefoneDto dto
    ) {
        return service.atualizarTelefone(id, dto);
    }

    @Operation(
            summary = "Remove telefone",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @DeleteMapping("/telefone/{id}")
    public ResponseEntity<Void> removerTelefone(
            @PathVariable Long id
    ) {
        return service.removerTelefone(id);
    }
}
