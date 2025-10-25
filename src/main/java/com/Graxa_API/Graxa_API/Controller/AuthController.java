package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Config.GerenciadorTokenJwt;
import com.Graxa_API.Graxa_API.Service.CredenciaisUsuarioService;
import com.Graxa_API.Graxa_API.Service.TelefoneService;
import com.Graxa_API.Graxa_API.Service.UsuarioService;
import com.Graxa_API.Graxa_API.dto.TelefoneDto.RequestTelefoneDto;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.RequestUsuarioDto;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.ResponseUsuarioDto;
import com.Graxa_API.Graxa_API.dto.credencialUsuarioDto.RequestCredenciaisUsuarioDto;
import com.Graxa_API.Graxa_API.dto.credencialUsuarioDto.RequestLoginDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CredenciaisUsuarioService credenciaisService;

    @Autowired
    private TelefoneService telefoneService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@Valid @RequestBody RequestUsuarioDto usuarioDto) {
        ResponseEntity<ResponseUsuarioDto> usuarioCriado = usuarioService.cadastrar(usuarioDto);

        if (usuarioCriado.getBody() == null) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar usuário");
        }

        Long usuarioId = usuarioCriado.getBody().id();

        // Criar credenciais
        RequestCredenciaisUsuarioDto credenciaisDto = new RequestCredenciaisUsuarioDto(
                usuarioDto.nomeUsuario(),
                usuarioId,
                usuarioDto.email(),
                usuarioDto.senha()
        );
        credenciaisService.criarCredencial(credenciaisDto);

        // Criar telefone
        RequestTelefoneDto telefoneDto = usuarioDto.telefone();
        if (telefoneDto != null) {
            telefoneService.criarTelefoneParaUsuario(usuarioId, telefoneDto);
        }

        // Autenticar e gerar token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioDto.email(), usuarioDto.senha())
        );
        String token = gerenciadorTokenJwt.generateToken(authentication);

        // Retornar token + dados do usuário
        return ResponseEntity.ok(
                Map.of(
                        "usuario", usuarioCriado.getBody(),
                        "token", token
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestLoginDto dto) {
        return credenciaisService.login(dto.identificador(), dto.senha());
    }
}
