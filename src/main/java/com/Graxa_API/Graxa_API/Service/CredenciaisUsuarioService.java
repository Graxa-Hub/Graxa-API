package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Config.GerenciadorTokenJwt;
import com.Graxa_API.Graxa_API.Entity.CredenciaisUsuarioEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Exception.CredencialNaoEncontradaException;
import com.Graxa_API.Graxa_API.Exception.LoginInvalidoException;
import com.Graxa_API.Graxa_API.Exception.NomeUsuarioDuplicadoException;
import com.Graxa_API.Graxa_API.Exception.UsuarioNaoEncontradoException;
import com.Graxa_API.Graxa_API.Repository.CredenciaisUsuarioRepository;
import com.Graxa_API.Graxa_API.Repository.ColaboradorRepository;
import com.Graxa_API.Graxa_API.dto.credencialUsuarioDto.CredencialUsuarioDetailsDto;
import com.Graxa_API.Graxa_API.dto.credencialUsuarioDto.RequestCredenciaisUsuarioDto;
import com.Graxa_API.Graxa_API.dto.credencialUsuarioDto.ResponseCredenciaisUsuarioDto;
import com.Graxa_API.Graxa_API.dto.credencialUsuarioDto.ResponseLoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CredenciaisUsuarioService {

    private final CredenciaisUsuarioRepository repository;
    private final ColaboradorRepository colaboradorRepository;
    private final EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;
    @Autowired
    private AuthenticationManager authenticatorManager;
    public CredenciaisUsuarioService(
            CredenciaisUsuarioRepository repository,
            ColaboradorRepository colaboradorRepository, EmailService emailService
    ) {
        this.repository = repository;
        this.colaboradorRepository = colaboradorRepository;
        this.emailService = emailService;
    }

    public ResponseEntity<?> resetarSenha(String email, String novaSenha) {
        Optional<CredenciaisUsuarioEntity> credOpt = repository.findByEmail(email);

        if (credOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("E-mail inválido.");
        }

        CredenciaisUsuarioEntity cred = credOpt.get();

        // Atualiza e limpa código
        cred.setSenha(passwordEncoder.encode(novaSenha));
        cred.setCodigoRecuperacao(null);
        cred.setCodigoExpiraEm(null);

        repository.save(cred);

        return ResponseEntity.ok("Senha alterada com sucesso!");
    }

    public ResponseEntity<?> validarCodigo(String email, String codigo) {
        Optional<CredenciaisUsuarioEntity> credencialOpt = repository.findByEmail(email);

        if (credencialOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("E-mail inválido.");
        }

        CredenciaisUsuarioEntity cred = credencialOpt.get();

        if (cred.getCodigoRecuperacao() == null) {
            return ResponseEntity.badRequest().body("Nenhum código solicitado.");
        }

        if (!cred.getCodigoRecuperacao().equals(codigo)) {
            return ResponseEntity.badRequest().body("Código inválido.");
        }

        if (cred.getCodigoExpiraEm().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Código expirado.");
        }

        return ResponseEntity.ok("Código válido.");
    }

    public ResponseEntity<?> enviarCodigoRecuperacao(String email) {
        Optional<CredenciaisUsuarioEntity> credencialOpt = repository.findByEmail(email);

        if (credencialOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("E-mail inválido ou não cadastrado.");
        }

        CredenciaisUsuarioEntity credencial = credencialOpt.get();

        // gerar código de recuperação
        String codigo = gerarCodigo();

        // salvar no banco
        credencial.setCodigoRecuperacao(codigo);
        credencial.setCodigoExpiraEm(LocalDateTime.now().plusMinutes(10));

        repository.save(credencial);

        // enviar e-mail (apenas exemplo)
        emailService.enviar(
                credencial.getEmail(),
                "Código de Recuperação de Senha",
                "Seu código de recuperação é: " + codigo
        );

        return ResponseEntity.ok("Código enviado para o e-mail informado.");
    }

    private String gerarCodigo() {
        int min = 100000;
        int max = 999999;
        return String.valueOf((int)(Math.random() * (max - min + 1)) + min);
    }

    // Buscar credencial por ID
    public ResponseEntity<?> getCredencial(Long id) {
        CredenciaisUsuarioEntity credencial = repository.findById(id)
                .orElseThrow(() -> new CredencialNaoEncontradaException(id));

        return ResponseEntity.ok(ResponseCredenciaisUsuarioDto.toResponse(credencial));
    }

    // Criar nova credencial (senha armazenada em texto plano)
    public ResponseEntity<?> criarCredencial(RequestCredenciaisUsuarioDto dto) {
        ColaboradorEntity usuario = colaboradorRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(dto.usuarioId()));

        if (repository.findByNomeUsuario(dto.nomeUsuario()).isPresent()) {
            throw new NomeUsuarioDuplicadoException(dto.nomeUsuario());
        }

        CredenciaisUsuarioEntity novaCredencial = new CredenciaisUsuarioEntity();
        novaCredencial.setUsuario(usuario);
        novaCredencial.setNomeUsuario(dto.nomeUsuario());
        novaCredencial.setEmail(dto.email());
        novaCredencial.setSenha(passwordEncoder.encode(dto.senha()));


        CredenciaisUsuarioEntity salva = repository.save(novaCredencial);
        return ResponseEntity.ok(ResponseCredenciaisUsuarioDto.toResponse(salva));
    }

    // Atualizar credencial existente (sem alterar o usuário)
    public ResponseEntity<?> updateCredencial(Long id, RequestCredenciaisUsuarioDto dto) {
        CredenciaisUsuarioEntity credencial = repository.findById(id)
                .orElseThrow(() -> new CredencialNaoEncontradaException(id));

        if (!credencial.getUsuario().getId().equals(dto.usuarioId())) {
            throw new UsuarioNaoEncontradoException(dto.usuarioId());
        }

        credencial.setNomeUsuario(dto.nomeUsuario());
        credencial.setEmail(dto.email());
        credencial.setSenha(passwordEncoder.encode(dto.senha()));

        CredenciaisUsuarioEntity atualizada = repository.save(credencial);
        return ResponseEntity.ok(ResponseCredenciaisUsuarioDto.toResponse(atualizada));
    }

    // Deletar credencial por ID
    public ResponseEntity<?> deletarCredencial(Long id) {
        CredenciaisUsuarioEntity credencial = repository.findById(id)
                .orElseThrow(() -> new CredencialNaoEncontradaException(id));

        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }





    public ResponseEntity<?> login(String identificador, String senha) {
        // Autentica o usuário e recebe o Authentication diretamente
        UsernamePasswordAuthenticationToken credentials =
                new UsernamePasswordAuthenticationToken(identificador, senha);

        Authentication authentication = authenticationManager.authenticate(credentials);

        // Usa o principal retornado pela autenticação
        CredencialUsuarioDetailsDto usuarioLogado = (CredencialUsuarioDetailsDto) authentication.getPrincipal();

        // Atualiza o último acesso
        CredenciaisUsuarioEntity entidade = repository.findById(usuarioLogado.usuarioId())
                .orElseThrow(LoginInvalidoException::new);

        entidade.setDataHoraUltimoAcesso(LocalDateTime.now());
        repository.save(entidade);

        // Gera o token JWT
        String token = gerenciadorTokenJwt.generateToken(authentication);

        return ResponseEntity.ok(ResponseLoginDto.toResponse(entidade, token));
    }


}
