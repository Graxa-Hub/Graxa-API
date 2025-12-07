package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.Evento.HotelEventoEntity;
import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Repository.ColaboradorRepository;
import com.Graxa_API.Graxa_API.Repository.HotelEventoRepository;
import com.Graxa_API.Graxa_API.Repository.ShowRepository;
import com.Graxa_API.Graxa_API.dto.Hotel.HotelEventoCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelEventoService {

    @Autowired
    private HotelEventoRepository hotelEventoRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    public HotelEventoEntity criar(HotelEventoCreateDTO dto) {

        ShowEntity show = showRepository.findById(dto.getShowId())
                .orElseThrow(() -> new RuntimeException("Show não encontrado."));

        ColaboradorEntity colaborador = colaboradorRepository.findById(dto.getColaboradorId())
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado."));

        HotelEventoEntity h = new HotelEventoEntity();

        h.setShow(show);
        h.setColaborador(colaborador);
        h.setNomeHotel(dto.getNomeHotel());
        h.setEndereco(dto.getEndereco());
        h.setLatitude(dto.getLatitude());
        h.setLongitude(dto.getLongitude());
        h.setDistanciaPalcoKm(dto.getDistanciaPalcoKm());
        h.setDistanciaAeroportoKm(dto.getDistanciaAeroportoKm());
        h.setCheckin(dto.getCheckin());
        h.setCheckout(dto.getCheckout());

        return hotelEventoRepository.save(h);
    }

    public List<HotelEventoEntity> listarPorShow(Long showId) {
        return hotelEventoRepository.findByShowId(showId);
    }

    public void remover(Long id) {
        hotelEventoRepository.deleteById(id);
    }

    public HotelEventoEntity atualizar(Long id, HotelEventoEntity dto) {
        HotelEventoEntity existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel não encontrado"));

        existente.setNomeHotel(dto.getNomeHotel());
        existente.setEndereco(dto.getEndereco());
        existente.setLatitude(dto.getLatitude());
        existente.setLongitude(dto.getLongitude());
        existente.setDistanciaPalcoKm(dto.getDistanciaPalcoKm());
        existente.setDistanciaAeroportoKm(dto.getDistanciaAeroportoKm());
        existente.setCheckin(dto.getCheckin());
        existente.setCheckout(dto.getCheckout());

        return repo.save(existente);
    }
}
