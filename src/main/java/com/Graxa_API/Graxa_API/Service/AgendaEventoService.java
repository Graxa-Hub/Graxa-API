package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.Evento.AgendaEventoEntity;
import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.Enums.TipoAgendaItem;
import com.Graxa_API.Graxa_API.Repository.AgendaEventoRepository;
import com.Graxa_API.Graxa_API.Repository.ShowRepository;
import com.Graxa_API.Graxa_API.dto.Agenda.AgendaEventoCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaEventoService {

    @Autowired
    private AgendaEventoRepository agendaEventoRepository;

    @Autowired
    private ShowRepository showRepository;

    public AgendaEventoEntity criar(AgendaEventoCreateDTO dto) {

        ShowEntity show = showRepository.findById(dto.showId())
                .orElseThrow(() -> new RuntimeException("Show não encontrado."));

        AgendaEventoEntity a = new AgendaEventoEntity();
        a.setShow(show);
        a.setTitulo(dto.titulo());
        a.setDescricao(dto.descricao());
        a.setDataHoraInicio(dto.dataHoraInicio());
        a.setDataHoraFim(dto.dataHoraFim());
        a.setOrdem(dto.ordem());

        if (dto.tipo() != null) {
            a.setTipo(TipoAgendaItem.fromValue(String.valueOf(dto.tipo())));
        }

        return agendaEventoRepository.save(a);
    }

    public List<AgendaEventoEntity> listarPorShow(Long showId) {
        return agendaEventoRepository.findByShowIdOrderByOrdemAscDataHoraInicioAsc(showId);
    }

    public void remover(Long id) {
        agendaEventoRepository.deleteById(id);
    }

    public AgendaEventoEntity atualizar(Long id, AgendaEventoCreateDTO dto) {
        AgendaEventoEntity existente = agendaEventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agenda não encontrada"));

        existente.setTitulo(dto.titulo());
        existente.setDescricao(dto.descricao());
        existente.setDataHoraInicio(dto.dataHoraInicio());
        existente.setDataHoraFim(dto.dataHoraFim());
        existente.setOrdem(dto.ordem());

        if (dto.tipo() != null) {
            existente.setTipo(TipoAgendaItem.fromValue(String.valueOf(dto.tipo())));
        }

        return agendaEventoRepository.save(existente);
    }
}
