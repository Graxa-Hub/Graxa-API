package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.Entity.Evento.VooEventoEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Repository.ColaboradorRepository;
import com.Graxa_API.Graxa_API.Repository.ShowRepository;
import com.Graxa_API.Graxa_API.Repository.VooEventoRepository;
import com.Graxa_API.Graxa_API.dto.Voo.VooEventoCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VooEventoService {

    @Autowired
    private VooEventoRepository vooEventoRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    public VooEventoEntity criar(VooEventoCreateDTO dto) {

        ShowEntity show = showRepository.findById(dto.getShowId())
                .orElseThrow(() -> new RuntimeException("Show não encontrado."));

        ColaboradorEntity colaborador = colaboradorRepository.findById(dto.getColaboradorId())
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado."));

        VooEventoEntity v = new VooEventoEntity();

        v.setShow(show);
        v.setColaborador(colaborador);

        v.setCiaAerea(dto.getCiaAerea());
        v.setCodigoVoo(dto.getCodigoVoo());
        v.setOrigem(dto.getOrigem());
        v.setDestino(dto.getDestino());
        v.setPartida(dto.getPartida());
        v.setChegada(dto.getChegada());

        return vooEventoRepository.save(v);
    }

    public List<VooEventoEntity> listarPorShow(Long showId) {
        return vooEventoRepository.findByShowId(showId);
    }

    public void remover(Long id) {
        vooEventoRepository.deleteById(id);
    }

    public VooEventoEntity atualizar(Long id, VooEventoEntity dto) {
        VooEventoEntity existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Voo não encontrado"));

        existente.setCiaAerea(dto.getCiaAerea());
        existente.setCodigoVoo(dto.getCodigoVoo());
        existente.setOrigem(dto.getOrigem());
        existente.setDestino(dto.getDestino());
        existente.setPartida(dto.getPartida());
        existente.setChegada(dto.getChegada());

        return repo.save(existente);
    }

}
