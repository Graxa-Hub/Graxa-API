package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.Entity.Evento.TransporteEventoEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Repository.TransporteEventoRepository;
import com.Graxa_API.Graxa_API.Repository.ShowRepository;
import com.Graxa_API.Graxa_API.Repository.ColaboradorRepository;
import com.Graxa_API.Graxa_API.dto.Transporte.TransporteEventoCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransporteEventoService {

    @Autowired
    private TransporteEventoRepository transporteEventoRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    public TransporteEventoEntity criar(TransporteEventoCreateDTO dto) {

        ShowEntity show = showRepository.findById(dto.getShowId())
                .orElseThrow(() -> new RuntimeException("Show não encontrado."));

        ColaboradorEntity colaborador = colaboradorRepository.findById(dto.getColaboradorId())
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado."));

        TransporteEventoEntity t = new TransporteEventoEntity();

        t.setShow(show);
        t.setColaborador(colaborador);

        t.setTipo(dto.getTipo());
        t.setSaida(dto.getSaida());
        t.setDestino(dto.getDestino());
        t.setMotorista(dto.getMotorista());
        t.setObservacao(dto.getObservacao());

        return transporteEventoRepository.save(t);
    }

    public List<TransporteEventoEntity> listarPorShow(Long showId) {
        return transporteEventoRepository.findByShowId(showId);
    }

    public void remover(Long id) {
        transporteEventoRepository.deleteById(id);
    }
}
