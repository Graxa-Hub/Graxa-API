package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.TurneEntity;
import com.Graxa_API.Graxa_API.Exception.BandaNaoEncontradaException;
import com.Graxa_API.Graxa_API.Exception.TurneJaExistenteException;
import com.Graxa_API.Graxa_API.Exception.TurneNaoEncontradaException;
import com.Graxa_API.Graxa_API.Repository.BandaRepository;
import com.Graxa_API.Graxa_API.Repository.TurneRepository;
import com.Graxa_API.Graxa_API.dto.TurneDto.RequestTurneDto;
import com.Graxa_API.Graxa_API.dto.TurneDto.ResponseTurneDto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurneService {

    private final TurneRepository turneRepository;
    private final BandaRepository bandaRepository; // ✅ injetar também

    public TurneService(TurneRepository turneRepository, BandaRepository bandaRepository) {
        this.turneRepository = turneRepository;
        this.bandaRepository = bandaRepository;
    }

    @Transactional
    public ResponseEntity<ResponseTurneDto> criarTurne(RequestTurneDto dto, String nomeImagem) {
        if (turneRepository.existsByNomeTurne(dto.nomeTurne())) {
            throw new TurneJaExistenteException(dto.nomeTurne());
        }

        var banda = bandaRepository.findById(dto.bandaId())
                .orElseThrow(() -> new BandaNaoEncontradaException(dto.bandaId()));

        TurneEntity turne = new TurneEntity();
        turne.setNomeTurne(dto.nomeTurne());
        turne.setDataHoraInicioTurne(dto.dataHoraInicioTurne());
        turne.setDataHoraFimTurne(dto.dataHoraFimTurne());
        turne.setDescricao(dto.descricao());
        turne.setAtivo(true);
        turne.setNomeImagem(nomeImagem);
        turne.setBanda(banda);

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
    public ResponseEntity<ResponseTurneDto> atualizarTurne(Long id, RequestTurneDto dto, String nomeImagem) {
        TurneEntity turne = turneRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new TurneNaoEncontradaException(id));

        var banda = bandaRepository.findById(dto.bandaId())
                .orElseThrow(() -> new BandaNaoEncontradaException(dto.bandaId()));

        turne.setNomeTurne(dto.nomeTurne());
        turne.setDataHoraInicioTurne(dto.dataHoraInicioTurne());
        turne.setDataHoraFimTurne(dto.dataHoraFimTurne());
        turne.setDescricao(dto.descricao());
        turne.setBanda(banda); // ✅ atualizar banda

        if (nomeImagem != null && !nomeImagem.isBlank()) {
            turne.setNomeImagem(nomeImagem);
        }

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


        return ResponseEntity.ok(turnes);
    }
}
