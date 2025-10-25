package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Service.TelefoneService;
import com.Graxa_API.Graxa_API.Service.UsuarioService;
import com.Graxa_API.Graxa_API.Service.CredenciaisUsuarioService;
import com.Graxa_API.Graxa_API.dto.TelefoneDto.RequestTelefoneDto;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.RequestUsuarioDto;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.ResponseUsuarioDto;
import com.Graxa_API.Graxa_API.dto.credencialUsuarioDto.RequestCredenciaisUsuarioDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final CredenciaisUsuarioService credenciaisService;
    private final TelefoneService telefoneService;

    public UsuarioController(UsuarioService usuarioService, CredenciaisUsuarioService credenciaisService, TelefoneService telefoneService) {
        this.usuarioService = usuarioService;
        this.credenciaisService = credenciaisService;
        this.telefoneService = telefoneService;
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ResponseUsuarioDto>> getUsuarios() {
        return usuarioService.getUsuarios();
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseUsuarioDto> getUsuarioPorId(@PathVariable Long id) {
        return usuarioService.getUsuarioPorId(id);
    }
    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseUsuarioDto> cadastrar(@Valid @RequestBody RequestUsuarioDto usuarioDto) {
        ResponseEntity<ResponseUsuarioDto> usuarioCriado = usuarioService.cadastrar(usuarioDto);

        if (usuarioCriado.getBody() != null) {
            Long usuarioId = usuarioCriado.getBody().id();

            // criar credenciais
            RequestCredenciaisUsuarioDto credenciaisDto = new RequestCredenciaisUsuarioDto(
                    usuarioDto.nomeUsuario(),
                    usuarioId,
                    usuarioDto.email(),
                    usuarioDto.senha()
            );
            credenciaisService.criarCredencial(credenciaisDto);


            RequestTelefoneDto telefoneDto = usuarioDto.telefone();
            if (telefoneDto != null) {
                telefoneService.criarTelefoneParaUsuario(usuarioId, telefoneDto);
            }
        }

        return usuarioCriado;
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseUsuarioDto> atualizar(@PathVariable Long id, @RequestBody RequestUsuarioDto usuarioDto) {
        return usuarioService.atualizar(id, usuarioDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        return usuarioService.desativar(id);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<ResponseUsuarioDto>> getUsuariosAtivos() {
        return usuarioService.getUsuariosAtivos();
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ResponseUsuarioDto> findUsuarioPorCpf(@PathVariable String cpf) {
        return usuarioService.findUsuarioPorCpf(cpf);
    }

    @GetMapping("/tipo")
    public ResponseEntity<List<ResponseUsuarioDto>> findUsuariosPorTipo(@RequestParam List<String> tipos) {
        return usuarioService.findUsuariosPorTipo(tipos);
    }
}
