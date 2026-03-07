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


@Service
public class CredenciaisUsuarioService {

    private final CredenciaisUsuarioRepository repository;
    private final ColaboradorRepository colaboradorRepository;

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
            ColaboradorRepository colaboradorRepository
    ) {
        this.repository = repository;
        this.colaboradorRepository = colaboradorRepository;
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
        System.out.println(authentication);
        // Usa o principal retornado pela autenticação
        CredencialUsuarioDetailsDto usuarioLogado = (CredencialUsuarioDetailsDto) authentication.getPrincipal();

        // Atualiza o último acesso
        CredenciaisUsuarioEntity entidade = repository.findByUsuarioId(usuarioLogado.usuarioId())
                .orElseThrow(LoginInvalidoException::new);

        entidade.setDataHoraUltimoAcesso(LocalDateTime.now());
        repository.save(entidade);

        // Gera o token JWT
        String token = gerenciadorTokenJwt.generateToken(authentication);

        return ResponseEntity.ok(ResponseLoginDto.toResponse(entidade, token));
    }


}
