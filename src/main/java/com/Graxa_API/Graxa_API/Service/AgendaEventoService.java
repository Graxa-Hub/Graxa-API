package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.Evento.AgendaEventoEntity;
import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Repository.AgendaEventoRepository;
import com.Graxa_API.Graxa_API.Repository.ShowRepository;
import com.Graxa_API.Graxa_API.Repository.ColaboradorRepository;
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

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    public AgendaEventoEntity criar(AgendaEventoCreateDTO dto) {

        ShowEntity show = showRepository.findById(dto.getShowId())
                .orElseThrow(() -> new RuntimeException("Show não encontrado."));

        ColaboradorEntity colaborador = null;
        if (dto.getColaboradorId() != null) {
            colaborador = colaboradorRepository.findById(dto.getColaboradorId())
                    .orElseThrow(() -> new RuntimeException("Colaborador não encontrado."));
        }

        AgendaEventoEntity a = new AgendaEventoEntity();
        a.setShow(show);
        a.setColaborador(colaborador);
        a.setTitulo(dto.getTitulo());
        a.setDescricao(dto.getDescricao());
        a.setDataHora(dto.getDataHora());
        a.setDuracaoMinutos(dto.getDuracaoMinutos());
        a.setOrdem(dto.getOrdem());

        return agendaEventoRepository.save(a);
    }

    public List<AgendaEventoEntity> listarPorShow(Long showId) {
        return agendaEventoRepository.findByShowIdOrderByOrdemAscDataHoraAsc(showId);
    }

    public void remover(Long id) {
        agendaEventoRepository.deleteById(id);
    }
}
