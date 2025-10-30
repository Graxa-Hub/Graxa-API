package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.EnderecoEntity;
import com.Graxa_API.Graxa_API.Entity.LocalEntity;
import com.Graxa_API.Graxa_API.Exception.EnderecoNaoEncontradoException;
import com.Graxa_API.Graxa_API.Exception.LocalNaoEncontradoException;
import com.Graxa_API.Graxa_API.Repository.EnderecoRepository;
import com.Graxa_API.Graxa_API.Repository.LocalRepository;
import com.Graxa_API.Graxa_API.dto.EnderecoDto.RequestEnderecoDto;
import com.Graxa_API.Graxa_API.dto.EnderecoDto.ResponseEnderecoDto;
import com.Graxa_API.Graxa_API.dto.LocalDto.RequestLocalDto;
import com.Graxa_API.Graxa_API.dto.LocalDto.ResponseLocalDto;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalService {


    private final LocalRepository localRepository;
    private final EnderecoRepository enderecoRepository;

    public LocalService(LocalRepository localRepository, EnderecoRepository enderecoRepository) {
        this.localRepository = localRepository;
        this.enderecoRepository = enderecoRepository;
    }

    @Transactional
    public ResponseEntity<ResponseLocalDto> criarLocal(RequestLocalDto dto) {
        LocalEntity local = new LocalEntity();

        local.setNome(dto.nome());
        local.setCapacidade(dto.capacidade());

        EnderecoEntity endereco = enderecoRepository.findById(dto.idEndereco())
                .orElseThrow(() -> new EnderecoNaoEncontradoException(dto.idEndereco()));

        local.setEndereco(endereco);


        LocalEntity salvo = localRepository.save(local);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseLocalDto.toResponse(salvo));
    }

    public ResponseEntity<ResponseLocalDto> buscarPorId(Long id) {
        LocalEntity local = localRepository.findById(id)
                .orElseThrow(() -> new LocalNaoEncontradoException(id));
        return ResponseEntity.ok(ResponseLocalDto.toResponse(local));
    }

    @Transactional
    public ResponseEntity<ResponseLocalDto> atualizarLocal(Long id, RequestLocalDto dto) {
        LocalEntity local = localRepository.findById(id)
                .orElseThrow(() -> new LocalNaoEncontradoException(id));

        EnderecoEntity endereco = enderecoRepository.findById(dto.idEndereco())
                .orElseThrow(() -> new EnderecoNaoEncontradoException(dto.idEndereco()));

        local.setNome(dto.nome());
        local.setCapacidade(dto.capacidade());
        local.setEndereco(endereco);

        LocalEntity atualizado = localRepository.save(local);
        return ResponseEntity.ok(ResponseLocalDto.toResponse(atualizado));
    }


    @Transactional
    public ResponseEntity<Void> deletarLocal(Long id) {
        LocalEntity local = localRepository.findById(id)
                .orElseThrow(() -> new LocalNaoEncontradoException(id));

        localRepository.delete(local);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<ResponseLocalDto>> listarTodos() {
        List<LocalEntity> locais = localRepository.findAll();

        if (locais.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ResponseLocalDto.toResponse(locais));
    }
}
