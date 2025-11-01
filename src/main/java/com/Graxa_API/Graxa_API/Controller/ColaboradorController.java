package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.ColaboradorService;
import com.Graxa_API.Graxa_API.Service.CredenciaisUsuarioService;
import com.Graxa_API.Graxa_API.Service.TelefoneService;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.RequestUsuarioDto;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.ResponseUsuarioDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colaboradores")
@Tag(name = "Colaboradores", description = "Endpoints relacionados à gestão de usuários colaboradores")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;
    private final CredenciaisUsuarioService credenciaisService;
    private final TelefoneService telefoneService;

    public ColaboradorController(
            ColaboradorService colaboradorService,
            CredenciaisUsuarioService credenciaisService,
            TelefoneService telefoneService
    ) {
        this.colaboradorService = colaboradorService;
        this.credenciaisService = credenciaisService;
        this.telefoneService = telefoneService;
    }

    @Operation(summary = "Lista todos os colaboradores", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    public ResponseEntity<List<ResponseUsuarioDto>> listarTodos() {
        return colaboradorService.listarTodos();
    }

    @Operation(summary = "Busca colaborador pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<ResponseUsuarioDto> buscarPorId(@PathVariable Long id) {
        return colaboradorService.buscarPorId(id);
    }

    @Operation(summary = "Atualiza os dados de um colaborador", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<ResponseUsuarioDto> atualizar(@PathVariable Long id, @RequestBody RequestUsuarioDto dto) {
        return colaboradorService.atualizar(id, dto);
    }

    @Operation(summary = "Desativa um colaborador pelo ID", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        return colaboradorService.desativar(id);
    }

    @Operation(summary = "Lista todos os colaboradores ativos", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/ativos")
    public ResponseEntity<List<ResponseUsuarioDto>> listarAtivos() {
        return colaboradorService.listarAtivos();
    }

    @Operation(summary = "Busca colaborador pelo CPF", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ResponseUsuarioDto> buscarPorCpf(@PathVariable String cpf) {
        return colaboradorService.buscarPorCpf(cpf);
    }

    @Operation(summary = "Busca colaboradores por tipo de usuário", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/tipo")
    public ResponseEntity<List<ResponseUsuarioDto>> buscarPorTipos(@RequestParam List<String> tipos) {
        return colaboradorService.buscarPorTipos(tipos);
    }
}
