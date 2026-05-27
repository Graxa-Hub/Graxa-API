package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Exception.CpfDuplicadoException;
import com.Graxa_API.Graxa_API.Exception.UsuarioNaoEncontradoException;
import com.Graxa_API.Graxa_API.Exception.UsuariosNaoEncontradosException;
import com.Graxa_API.Graxa_API.Factory.UsuarioFactory;
import com.Graxa_API.Graxa_API.Repository.ColaboradorRepository;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.RequestUsuarioDto;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.ResponseUsuarioDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColaboradorService {

    private final ColaboradorRepository repository;
    private final UsuarioFactory factory;

    public ColaboradorService(ColaboradorRepository repository, UsuarioFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    public ResponseEntity<List<ResponseUsuarioDto>> listarTodos() {
        List<ColaboradorEntity> usuarios = repository.findAll();
        if (usuarios.isEmpty()) {
            throw new UsuariosNaoEncontradosException();
        }
        return ResponseEntity.ok(ResponseUsuarioDto.toResponse(usuarios));
    }

    public ResponseEntity<ResponseUsuarioDto> buscarPorId(Long id) {
        ColaboradorEntity usuario = repository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        return ResponseEntity.ok(ResponseUsuarioDto.toResponse(usuario));
    }

    public ResponseEntity<ResponseUsuarioDto> cadastrar(RequestUsuarioDto dto, String ipConsentimento) {
        if (repository.existsByCpfAllIgnoreCase(dto.cpf())) {
            throw new CpfDuplicadoException();
        }

        ColaboradorEntity colaborador = (ColaboradorEntity) factory.criarUsuario(dto, ipConsentimento);
        ColaboradorEntity salvo = repository.save(colaborador);
        return ResponseEntity.status(201).body(ResponseUsuarioDto.toResponse(salvo));
    }

    public ResponseEntity<ResponseUsuarioDto> atualizar(Long id, RequestUsuarioDto dto) {
        ColaboradorEntity usuario = repository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        if (dto.nome() != null) usuario.setNome(dto.nome());
        if (dto.cpf() != null) usuario.setCpf(dto.cpf());
        if (dto.dataNascimento() != null) usuario.setDataNascimento(dto.dataNascimento());
        if (dto.tipoUsuario() != null) usuario.setTipoUsuario(dto.tipoUsuario());
        if (dto.fotoNome() != null && !dto.fotoNome().isBlank()) {
            usuario.setFotoNome(dto.fotoNome());
        }

        ColaboradorEntity atualizado = repository.save(usuario);
        return ResponseEntity.ok(ResponseUsuarioDto.toResponse(atualizado));
    }

    public ResponseEntity<Void> desativar(Long id) {
        ColaboradorEntity usuario = repository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        usuario.setAtivo(false);
        repository.save(usuario);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<ResponseUsuarioDto>> listarAtivos() {
        List<ColaboradorEntity> ativos = repository.findByAtivoTrueOrderByNomeAsc();
        return ResponseEntity.ok(ResponseUsuarioDto.toResponse(ativos));
    }

    public ResponseEntity<ResponseUsuarioDto> buscarPorCpf(String cpf) {
        List<ColaboradorEntity> usuarios = repository.findByCpf(cpf);
        if (usuarios.isEmpty()) {
            throw new UsuarioNaoEncontradoException("CPF: " + cpf);
        }
        return ResponseEntity.ok(ResponseUsuarioDto.toResponse(usuarios.get(0)));
    }

    public ResponseEntity<List<ResponseUsuarioDto>> buscarPorTipos(List<String> tipos) {
        List<ColaboradorEntity> usuarios = repository.findByTipoUsuarioIn(tipos);
        return ResponseEntity.ok(ResponseUsuarioDto.toResponse(usuarios));
    }
}
