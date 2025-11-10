package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.Usuario.RepresentanteEntity;
import com.Graxa_API.Graxa_API.Exception.DuplicationException;
import com.Graxa_API.Graxa_API.Exception.EmailDuplicadoException;
import com.Graxa_API.Graxa_API.Exception.UsuarioNaoEncontradoException;
import com.Graxa_API.Graxa_API.Factory.UsuarioFactory;
import com.Graxa_API.Graxa_API.Repository.RepresentanteRepository;
import com.Graxa_API.Graxa_API.dto.RepresentanteDto.RequestRepresentanteDto;
import com.Graxa_API.Graxa_API.dto.RepresentanteDto.ResponseRepresentanteDto;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepresentanteService {

    private final RepresentanteRepository repository;
    private final UsuarioFactory factory;

    public RepresentanteService(RepresentanteRepository repository, UsuarioFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    public ResponseEntity<List<ResponseRepresentanteDto>> listarRepresentantes() {
        List<RepresentanteEntity> representantes = repository.findAll();
        return ResponseEntity.ok(ResponseRepresentanteDto.toResponse(representantes));
    }

    public ResponseEntity<ResponseRepresentanteDto> buscarPorId(Long id) {
        RepresentanteEntity representante = repository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        return ResponseEntity.ok(ResponseRepresentanteDto.toResponse(representante));
    }

    public ResponseEntity<ResponseRepresentanteDto> buscarPorEmail(String email) {
        RepresentanteEntity representante = repository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Email não encontrado: " + email));
        return ResponseEntity.ok(ResponseRepresentanteDto.toResponse(representante));
    }

    public ResponseEntity<ResponseRepresentanteDto> buscarPorNome(String nome) {
        RepresentanteEntity representante = repository.findByNomeContainingIgnoreCase(nome)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Nome não encontrado: " + nome));
        return ResponseEntity.ok(ResponseRepresentanteDto.toResponse(representante));
    }

    @Transactional
    public ResponseEntity<ResponseRepresentanteDto> criarRepresentante(RequestRepresentanteDto dto) {
        if(repository.existsByEmail(dto.email())){
            throw new EmailDuplicadoException(dto.email());
        }
        RepresentanteEntity representante = (RepresentanteEntity) factory.criarUsuario(dto);
        RepresentanteEntity salvo = repository.save(representante);
        return ResponseEntity.status(201).body(ResponseRepresentanteDto.toResponse(salvo));
    }

    @Transactional
    public ResponseEntity<ResponseRepresentanteDto> atualizarRepresentante(Long id, RequestRepresentanteDto dto) {
        RepresentanteEntity representante = repository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        representante.setNome(dto.nome());
        representante.setEmail(dto.email());

        RepresentanteEntity atualizado = repository.save(representante);
        return ResponseEntity.ok(ResponseRepresentanteDto.toResponse(atualizado));
    }

    @Transactional
    public ResponseEntity<Void> deletarRepresentante(Long id) {
        RepresentanteEntity representante = repository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        representante.setAtivo(false);
        repository.save(representante);

        return ResponseEntity.noContent().build();
    }
}