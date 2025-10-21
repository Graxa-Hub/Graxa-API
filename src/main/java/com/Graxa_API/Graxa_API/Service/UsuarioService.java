package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.UsuarioEntity;
import com.Graxa_API.Graxa_API.Exception.CpfDuplicadoException;
import com.Graxa_API.Graxa_API.Exception.UsuarioNaoEncontradoException;
import com.Graxa_API.Graxa_API.Exception.UsuariosNaoEncontradosException;
import com.Graxa_API.Graxa_API.Repository.UsuarioRepository;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.RequestUsuarioDto;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.ResponseUsuarioDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<List<ResponseUsuarioDto>> getUsuarios() {
        List<UsuarioEntity> usuarios = repository.findAll();
        if (usuarios.isEmpty()) {
            throw new UsuariosNaoEncontradosException();
        }
        return ResponseEntity.ok(ResponseUsuarioDto.toResponse(usuarios));
    }

    public ResponseEntity<ResponseUsuarioDto> getUsuarioPorId(Long id) {
        UsuarioEntity usuario = repository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        return ResponseEntity.ok(ResponseUsuarioDto.toResponse(usuario));
    }

    public ResponseEntity<ResponseUsuarioDto> cadastrar(RequestUsuarioDto usuarioDto) {
        if (repository.existsByCpfAllIgnoreCase(usuarioDto.cpf())) {
            throw new CpfDuplicadoException();
        }

        UsuarioEntity usuarioEntity = new UsuarioEntity(usuarioDto);
        usuarioEntity.setAtivo(true);

        UsuarioEntity saved = repository.save(usuarioEntity);
        return ResponseEntity.status(201).body(ResponseUsuarioDto.toResponse(saved));
    }

    public ResponseEntity<ResponseUsuarioDto> atualizar(Long id, RequestUsuarioDto usuarioDto) {
        UsuarioEntity usuario = repository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        if (usuarioDto.nome() != null) {
            usuario.setNome(usuarioDto.nome());
        }
        if (usuarioDto.cpf() != null) {
            usuario.setCpf(usuarioDto.cpf());
        }

        UsuarioEntity atualizado = repository.save(usuario);
        return ResponseEntity.ok(ResponseUsuarioDto.toResponse(atualizado));
    }

    public ResponseEntity<Void> desativar(Long id) {
        UsuarioEntity usuario = repository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        usuario.setAtivo(false);
        repository.save(usuario);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<ResponseUsuarioDto>> getUsuariosAtivos() {
        List<UsuarioEntity> ativos = repository.findByAtivoTrueOrderByNomeAsc();
        return ResponseEntity.ok(ResponseUsuarioDto.toResponse(ativos));
    }

    public ResponseEntity<ResponseUsuarioDto> findUsuarioPorCpf(String cpf) {
        List<UsuarioEntity> usuarios = repository.findByCpf(cpf);
        if (usuarios.isEmpty()) {
            throw new UsuarioNaoEncontradoException("CPF: " + cpf);
        }
        return ResponseEntity.ok(ResponseUsuarioDto.toResponse(usuarios.get(0)));
    }

    public ResponseEntity<List<ResponseUsuarioDto>> findUsuariosPorTipo(List<String> tipos) {
        List<UsuarioEntity> usuarios = repository.findByTipoUsuarioIn(tipos);
        return ResponseEntity.ok(ResponseUsuarioDto.toResponse(usuarios));
    }
}
