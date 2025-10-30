package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.ColaboradorService;
import com.Graxa_API.Graxa_API.Service.CredenciaisUsuarioService;
import com.Graxa_API.Graxa_API.Service.TelefoneService;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.RequestUsuarioDto;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.ResponseUsuarioDto;
import com.Graxa_API.Graxa_API.dto.TelefoneDto.RequestTelefoneDto;
import com.Graxa_API.Graxa_API.dto.credencialUsuarioDto.RequestCredenciaisUsuarioDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colaboradores")
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

    // 🔹 Listar todos os colaboradores
    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ResponseUsuarioDto>> listarTodos() {
        return colaboradorService.listarTodos();
    }

    // 🔹 Buscar colaborador por ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseUsuarioDto> buscarPorId(@PathVariable Long id) {
        return colaboradorService.buscarPorId(id);
    }

    // 🔹 Cadastrar novo colaborador
    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseUsuarioDto> cadastrar(@Valid @RequestBody RequestUsuarioDto dto) {
        ResponseEntity<ResponseUsuarioDto> colaboradorCriado = colaboradorService.cadastrar(dto);

        if (colaboradorCriado.getBody() != null) {
            Long colaboradorId = colaboradorCriado.getBody().id();

            // Criar credenciais
            RequestCredenciaisUsuarioDto credenciaisDto = new RequestCredenciaisUsuarioDto(
                    dto.nomeUsuario(),
                    colaboradorId,
                    dto.email(),
                    dto.senha()
            );
            credenciaisService.criarCredencial(credenciaisDto);

            // Criar telefone
            RequestTelefoneDto telefoneDto = dto.telefone();
            if (telefoneDto != null) {
                telefoneService.criarTelefoneParaUsuario(colaboradorId, telefoneDto);
            }
        }

        return colaboradorCriado;
    }

    // 🔹 Atualizar colaborador
    @PutMapping("/{id}")
    public ResponseEntity<ResponseUsuarioDto> atualizar(@PathVariable Long id, @RequestBody RequestUsuarioDto dto) {
        return colaboradorService.atualizar(id, dto);
    }

    // 🔹 Desativar colaborador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        return colaboradorService.desativar(id);
    }

    // 🔹 Listar colaboradores ativos
    @GetMapping("/ativos")
    public ResponseEntity<List<ResponseUsuarioDto>> listarAtivos() {
        return colaboradorService.listarAtivos();
    }

    // 🔹 Buscar colaborador por CPF
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ResponseUsuarioDto> buscarPorCpf(@PathVariable String cpf) {
        return colaboradorService.buscarPorCpf(cpf);
    }

    // 🔹 Buscar colaboradores por tipo
    @GetMapping("/tipo")
    public ResponseEntity<List<ResponseUsuarioDto>> buscarPorTipos(@RequestParam List<String> tipos) {
        return colaboradorService.buscarPorTipos(tipos);
    }
}
