package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.TelefoneEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Exception.TelefoneNaoEncontradoException;
import com.Graxa_API.Graxa_API.Exception.UsuarioNaoEncontradoException;
import com.Graxa_API.Graxa_API.Repository.TelefoneRepository;
import com.Graxa_API.Graxa_API.Repository.UsuarioRepository;
import com.Graxa_API.Graxa_API.dto.TelefoneDto.RequestTelefoneDto;
import com.Graxa_API.Graxa_API.dto.TelefoneDto.ResponseTelefoneDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TelefoneService {

    private final TelefoneRepository telefoneRepository;
    private final UsuarioRepository usuarioRepository;

    public TelefoneService(TelefoneRepository telefoneRepository, UsuarioRepository usuarioRepository) {
        this.telefoneRepository = telefoneRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ResponseEntity<ResponseTelefoneDto> criarTelefoneParaUsuario(Long usuarioId, RequestTelefoneDto dto) {
        ColaboradorEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));

        TelefoneEntity telefone = new TelefoneEntity();
        telefone.setUsuario(usuario);
        telefone.setTipoTelefone(dto.tipoTelefone());
        telefone.setNumeroTelefone(dto.numeroTelefone());

        TelefoneEntity salvo = telefoneRepository.save(telefone);
        return ResponseEntity.ok(ResponseTelefoneDto.toResponse(salvo));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResponseTelefoneDto> buscarPorId(Long id) {
        TelefoneEntity telefone = telefoneRepository.findById(id)
                .orElseThrow(() -> new TelefoneNaoEncontradoException(id));
        return ResponseEntity.ok(ResponseTelefoneDto.toResponse(telefone));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<ResponseTelefoneDto>> listarPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new UsuarioNaoEncontradoException(usuarioId);
        }
        List<TelefoneEntity> lista = telefoneRepository.findByUsuarioId(usuarioId);
        List<ResponseTelefoneDto> resp = lista.stream()
                .map(ResponseTelefoneDto::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }

    @Transactional
    public ResponseEntity<ResponseTelefoneDto> atualizarTelefone(Long id, RequestTelefoneDto dto) {
        TelefoneEntity telefone = telefoneRepository.findById(id)
                .orElseThrow(() -> new TelefoneNaoEncontradoException(id));

        telefone.setTipoTelefone(dto.tipoTelefone());
        telefone.setNumeroTelefone(dto.numeroTelefone());

        TelefoneEntity atualizado = telefoneRepository.save(telefone);
        return ResponseEntity.ok(ResponseTelefoneDto.toResponse(atualizado));
    }

    @Transactional
    public ResponseEntity<Void> removerTelefone(Long id) {
        if (!telefoneRepository.existsById(id)) {
            throw new TelefoneNaoEncontradoException(id);
        }
        telefoneRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
