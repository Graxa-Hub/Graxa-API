package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.Usuario.ArtistaEntity;
import com.Graxa_API.Graxa_API.Exception.UsuarioNaoEncontradoException;
import com.Graxa_API.Graxa_API.Factory.UsuarioFactory;
import com.Graxa_API.Graxa_API.Repository.ArtistaRepository;
import com.Graxa_API.Graxa_API.dto.ArtistaDto.RequestArtistaDto;
import com.Graxa_API.Graxa_API.dto.ArtistaDto.ResponseArtistaDto;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistaService {

    private final ArtistaRepository repository;
    private final UsuarioFactory factory;

    public ArtistaService(ArtistaRepository repository, UsuarioFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    public ResponseEntity<List<ResponseArtistaDto>> listarArtistas() {
        List<ArtistaEntity> artistas = repository.findAll();
        return ResponseEntity.ok(ResponseArtistaDto.toResponse(artistas));
    }

    public ResponseEntity<ResponseArtistaDto> buscarPorId(Long id) {
        ArtistaEntity artista = repository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        return ResponseEntity.ok(ResponseArtistaDto.toResponse(artista));
    }

    @Transactional
    public ResponseEntity<ResponseArtistaDto> criarArtista(RequestArtistaDto dto) {
        ArtistaEntity artista = (ArtistaEntity) factory.criarUsuario(dto);
        ArtistaEntity salvo = repository.save(artista);
        return ResponseEntity.status(201).body(ResponseArtistaDto.toResponse(salvo));
    }

    @Transactional
    public ResponseEntity<ResponseArtistaDto> atualizarArtista(Long id, RequestArtistaDto dto) {
        ArtistaEntity artista = repository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        artista.setNome(dto.nome());
        artista.setCpf(dto.cpf());
        artista.setFotoNome(dto.fotoNome());

        ArtistaEntity atualizado = repository.save(artista);
        return ResponseEntity.ok(ResponseArtistaDto.toResponse(atualizado));
    }

    @Transactional
    public ResponseEntity<Void> deletarArtista(Long id) {
        ArtistaEntity artista = repository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        artista.setAtivo(false);
        repository.save(artista);

        return ResponseEntity.noContent().build();
    }
}
