package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.TurneEntity;
import com.Graxa_API.Graxa_API.Exception.TurneJaExistenteException;
import com.Graxa_API.Graxa_API.Exception.TurneNaoEncontradaException;
import com.Graxa_API.Graxa_API.Repository.TurneRepository;
import com.Graxa_API.Graxa_API.dto.TurneDto.RequestTurneDto;
import com.Graxa_API.Graxa_API.dto.TurneDto.ResponseTurneDto;
import jakarta.transaction.Transactional;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurneService {

    private final TurneRepository turneRepository;

    public TurneService(TurneRepository turneRepository) {
        this.turneRepository = turneRepository;
    }

    @Transactional
    public ResponseEntity<ResponseTurneDto> criarTurne(RequestTurneDto dto) {
        if (turneRepository.existsByNomeTurne(dto.nomeTurne())) {
            throw new TurneJaExistenteException(dto.nomeTurne());
        }

        TurneEntity turne = new TurneEntity();
        turne.setNomeTurne(dto.nomeTurne());
        turne.setDataHoraInicioTurne(dto.dataHoraInicioTurne());
        turne.setDataHoraFimTurne(dto.dataHoraFimTurne());
        turne.setAtivo(true);

        TurneEntity salvo = turneRepository.save(turne);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseTurneDto.toResponse(salvo));
    }

    public ResponseEntity<ResponseTurneDto> buscarPorId(Long id) {
        TurneEntity turne = turneRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new TurneNaoEncontradaException(id));
        return ResponseEntity.ok(ResponseTurneDto.toResponse(turne));
    }

    public ResponseEntity<ResponseTurneDto> buscarPorNome(String nome) {
        TurneEntity turne = turneRepository.findByNomeTurneAndAtivoTrue(nome)
                .orElseThrow(() -> new TurneNaoEncontradaException(nome));
        return ResponseEntity.ok(ResponseTurneDto.toResponse(turne));
    }

    @Transactional
    public ResponseEntity<ResponseTurneDto> atualizarTurne(Long id, RequestTurneDto dto) {
        TurneEntity turne = turneRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new TurneNaoEncontradaException(id));

        turne.setNomeTurne(dto.nomeTurne());
        turne.setDataHoraInicioTurne(dto.dataHoraInicioTurne());
        turne.setDataHoraFimTurne(dto.dataHoraFimTurne());

        TurneEntity atualizado = turneRepository.save(turne);
        return ResponseEntity.ok(ResponseTurneDto.toResponse(atualizado));
    }

    @Transactional
    public ResponseEntity<Void> deletarTurne(Long id) {
        TurneEntity turne = turneRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new TurneNaoEncontradaException(id));

        turne.setAtivo(false);
        turneRepository.save(turne);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<ResponseTurneDto>> listarAtivas() {
        List<ResponseTurneDto> turnes = turneRepository.findAllByAtivoTrue()
                .stream()
                .map(ResponseTurneDto::toResponse)
                .toList();

        if (turnes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(turnes);
    }
}

