package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.EnderecoEntity;
import com.Graxa_API.Graxa_API.Exception.EnderecoNaoEncontradoException;
import com.Graxa_API.Graxa_API.Repository.EnderecoRepository;
import com.Graxa_API.Graxa_API.dto.EnderecoDto.RequestEnderecoDto;
import com.Graxa_API.Graxa_API.dto.EnderecoDto.ResponseEnderecoDto;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    @Transactional
    public ResponseEntity<ResponseEnderecoDto> criarEndereco(RequestEnderecoDto dto) {
        EnderecoEntity endereco = new EnderecoEntity();

        endereco.setTipoEndereco(dto.tipoEndereco());
        endereco.setCep(dto.cep());
        endereco.setLogradouro(dto.logradouro());
        endereco.setNumero(dto.numero());
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro());
        endereco.setCidade(dto.cidade());
        endereco.setEstado(dto.estado());
        endereco.setPais(dto.pais());

        EnderecoEntity salvo = enderecoRepository.save(endereco);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseEnderecoDto.toResponse(salvo));
    }

    public ResponseEntity<ResponseEnderecoDto> buscarPorId(Long id) {
        EnderecoEntity endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new EnderecoNaoEncontradoException(id));
        return ResponseEntity.ok(ResponseEnderecoDto.toResponse(endereco));
    }

    @Transactional
    public ResponseEntity<ResponseEnderecoDto> atualizarEndereco(Long id, RequestEnderecoDto dto) {
        EnderecoEntity endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new EnderecoNaoEncontradoException(id));

        endereco.setTipoEndereco(dto.tipoEndereco());
        endereco.setCep(dto.cep());
        endereco.setLogradouro(dto.logradouro());
        endereco.setNumero(dto.numero());
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro());
        endereco.setCidade(dto.cidade());
        endereco.setEstado(dto.estado());
        endereco.setPais(dto.pais());

        EnderecoEntity atualizado = enderecoRepository.save(endereco);
        return ResponseEntity.ok(ResponseEnderecoDto.toResponse(atualizado));
    }

    @Transactional
    public ResponseEntity<Void> deletarEndereco(Long id) {
        EnderecoEntity endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new EnderecoNaoEncontradoException(id));

        enderecoRepository.delete(endereco);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<ResponseEnderecoDto>> listarTodos() {
        List<EnderecoEntity> enderecos = enderecoRepository.findAll();

        if (enderecos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ResponseEnderecoDto.toResponseList(enderecos));
    }
}
