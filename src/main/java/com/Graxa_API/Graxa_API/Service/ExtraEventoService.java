package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.Evento.ExtraEventoEntity;
import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.Repository.ExtraEventoRepository;
import com.Graxa_API.Graxa_API.Repository.ShowRepository;
import com.Graxa_API.Graxa_API.dto.Extra.ExtraEventoCreateDTO;
import com.Graxa_API.Graxa_API.dto.Logistica.ExtraDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExtraEventoService {

    @Autowired
    private ExtraEventoRepository repository;

    @Autowired
    private ShowRepository showRepository;

    public ExtraDTO buscarPorShow(Long showId) {
        return repository.findByShowId(showId)
                .map(e -> new ExtraDTO(
                        e.getId(),
                        showId,
                        e.getObs(),
                        e.getContatos()
                ))
                .orElse(null);
    }

    public ExtraDTO criarOuAtualizar(ExtraEventoCreateDTO dto) {

        ShowEntity show = showRepository.findById(dto.showId())
                .orElseThrow(() -> new RuntimeException("Show não encontrado."));

        ExtraEventoEntity existente = repository.findByShowId(dto.showId()).orElse(null);

        // Se já existe → atualizar
        if (existente != null) {
            existente.setObs(dto.obs());
            existente.setContatos(dto.contatos());
            repository.save(existente);

            return new ExtraDTO(
                    existente.getId(),
                    existente.getShow().getId(),
                    existente.getObs(),
                    existente.getContatos()
            );
        }

        // Criar novo
        ExtraEventoEntity novo = new ExtraEventoEntity();
        novo.setShow(show);
        novo.setObs(dto.obs());
        novo.setContatos(dto.contatos());

        repository.save(novo);

        return new ExtraDTO(
                novo.getId(),
                show.getId(),
                novo.getObs(),
                novo.getContatos()
        );
    }
}
