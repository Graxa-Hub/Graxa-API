package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.CredenciaisUsuarioEntity;
import com.Graxa_API.Graxa_API.Entity.UsuarioEntity;
import com.Graxa_API.Graxa_API.Exception.CredencialNaoEncontradaException;
import com.Graxa_API.Graxa_API.Exception.LoginInvalidoException;
import com.Graxa_API.Graxa_API.Exception.NomeUsuarioDuplicadoException;
import com.Graxa_API.Graxa_API.Exception.SenhaInvalidaException;
import com.Graxa_API.Graxa_API.Exception.UsuarioNaoEncontradoException;
import com.Graxa_API.Graxa_API.Repository.CredenciaisUsuarioRepository;
import com.Graxa_API.Graxa_API.Repository.UsuarioRepository;
import com.Graxa_API.Graxa_API.dto.credencialUsuarioDto.RequestCredenciaisUsuarioDto;
import com.Graxa_API.Graxa_API.dto.credencialUsuarioDto.ResponseCredenciaisUsuarioDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CredenciaisUsuarioService {

    private final CredenciaisUsuarioRepository repository;
    private final UsuarioRepository usuarioRepository;

    public CredenciaisUsuarioService(
            CredenciaisUsuarioRepository repository,
            UsuarioRepository usuarioRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    // Buscar credencial por ID
    public ResponseEntity<?> getCredencial(Long id) {
        CredenciaisUsuarioEntity credencial = repository.findById(id)
                .orElseThrow(() -> new CredencialNaoEncontradaException(id));

        return ResponseEntity.ok(ResponseCredenciaisUsuarioDto.toResponse(credencial));
    }

    // Criar nova credencial (senha armazenada em texto plano)
    public ResponseEntity<?> criarCredencial(RequestCredenciaisUsuarioDto dto) {
        UsuarioEntity usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(dto.usuarioId()));

        if (repository.findByNomeUsuario(dto.nomeUsuario()).isPresent()) {
            throw new NomeUsuarioDuplicadoException(dto.nomeUsuario());
        }

        CredenciaisUsuarioEntity novaCredencial = new CredenciaisUsuarioEntity(
                usuario,
                dto.nomeUsuario(),
                dto.email(),
                dto.senha() // senha salva sem encoder
        );

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
        credencial.setSenha(dto.senha()); // senha atualizada sem encoder

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

    // Login por email ou nome de usuário (comparação simples)
    public ResponseEntity<?> login(String identificador, String senha) {
        Optional<CredenciaisUsuarioEntity> credencial = repository.findByEmail(identificador);

        if (credencial.isEmpty()) {
            credencial = repository.findByNomeUsuario(identificador);
        }

        if (credencial.isEmpty()) {
            throw new LoginInvalidoException();
        }

        CredenciaisUsuarioEntity usuarioCredencial = credencial.get();

        if (!senha.equals(usuarioCredencial.getSenha())) {
            throw new SenhaInvalidaException();
        }

        usuarioCredencial.setDataHoraUltimoAcesso(LocalDateTime.now());
        repository.save(usuarioCredencial);

        return ResponseEntity.ok(ResponseCredenciaisUsuarioDto.toResponse(usuarioCredencial));
    }
}
