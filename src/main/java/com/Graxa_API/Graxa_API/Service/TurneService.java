package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.TurneEntity;
import com.Graxa_API.Graxa_API.Exception.TurneJaExistenteException;
import com.Graxa_API.Graxa_API.Repository.TurneRepository;
import com.Graxa_API.Graxa_API.dto.TurneDto.RequestTurneDto;
import com.Graxa_API.Graxa_API.dto.TurneDto.ResponseTurneDto;
import jakarta.transaction.Transactional;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TurneService {
   private final TurneRepository turneRepository;

    public TurneService(TurneRepository turneRepository) {
        this.turneRepository = turneRepository;
    }

    @Transactional
    public ResponseEntity<ResponseTurneDto> criarTurne(RequestTurneDto dto) {

        if(turneRepository.existsByNomeTurne(dto.nomeTurne())) {
            throw new TurneJaExistenteException(dto.nomeTurne());
        }

        TurneEntity turne = new TurneEntity();

        turne.setNomeTurne(dto.nomeTurne());
        turne.setDataHoraInicioTurne(dto.dataHoraInicioTurne());
        turne.setDataHoraFimTurne(dto.dataHoraFimTurne());

        return ResponseEntity.status(201).body(ResponseTurneDto.toResponse(turneRepository.save(turne)));
    }
}
