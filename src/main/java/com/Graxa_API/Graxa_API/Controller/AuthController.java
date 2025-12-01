package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Config.GerenciadorTokenJwt;
import com.Graxa_API.Graxa_API.Service.CredenciaisUsuarioService;
import com.Graxa_API.Graxa_API.Service.TelefoneService;
import com.Graxa_API.Graxa_API.Service.ColaboradorService;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.RequestUsuarioDto;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.ResponseUsuarioDto;
import com.Graxa_API.Graxa_API.dto.credencialUsuarioDto.RequestLoginDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para registro e login de usuários")
public class AuthController {

    @Autowired
    private ColaboradorService colaboradorService;

    @Autowired
    private CredenciaisUsuarioService credenciaisService;

    @Autowired
    private TelefoneService telefoneService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Operation(summary = "Registra um novo usuário e retorna o token de autenticação")
    @PostMapping("/register")
    public ResponseEntity<?> registrar(@Valid @RequestBody RequestUsuarioDto usuarioDto) {
        ResponseEntity<ResponseUsuarioDto> usuarioCriado = colaboradorService.cadastrar(usuarioDto);

        if (usuarioCriado.getBody() == null) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar usuário");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioDto.email(), usuarioDto.senha())
        );
        String token = gerenciadorTokenJwt.generateToken(authentication);

        return ResponseEntity.ok(
                Map.of(
                        "usuario", usuarioCriado.getBody(),
                        "token", token
                )
        );
    }

    @Operation(summary = "Realiza login e retorna o token de autenticação")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestLoginDto dto) {

        return credenciaisService.login(dto.identificador(), dto.senha());
    }
}
