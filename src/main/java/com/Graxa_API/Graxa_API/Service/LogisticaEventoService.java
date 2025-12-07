package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Repository.*;
import com.Graxa_API.Graxa_API.dto.Logistica.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogisticaEventoService {

    @Autowired private HotelEventoRepository hotelRepo;
    @Autowired private VooEventoRepository vooRepo;
    @Autowired private TransporteEventoRepository transpRepo;
    @Autowired private AgendaEventoRepository agendaRepo;

    public LogisticaEventoResponseDTO buscarPorShow(Long showId) {

        var hotels = hotelRepo.findByShowId(showId).stream()
                .map(h -> new HotelDTO(
                        h.getId(),
                        h.getColaborador() != null ? h.getColaborador().getId() : null,
                        h.getNomeHotel(),
                        h.getEndereco(),
                        h.getLatitude(),
                        h.getLongitude(),
                        h.getDistanciaPalcoKm(),
                        h.getDistanciaAeroportoKm(),
                        h.getCheckin() != null ? h.getCheckin().toString() : null,
                        h.getCheckout() != null ? h.getCheckout().toString() : null
                )).toList();

        var voos = vooRepo.findByShowId(showId).stream()
                .map(v -> new VooDTO(
                        v.getId(),
                        v.getColaborador() != null ? v.getColaborador().getId() : null,
                        v.getCiaAerea(),
                        v.getCodigoVoo(),
                        v.getOrigem(),
                        v.getDestino(),
                        v.getPartida() != null ? v.getPartida().toString() : null,
                        v.getChegada() != null ? v.getChegada().toString() : null
                )).toList();

        var transportes = transpRepo.findByShowId(showId).stream()
                .map(t -> new TransporteDTO(
                        t.getId(),
                        t.getColaborador() != null ? t.getColaborador().getId() : null,
                        t.getTipo(),
                        t.getSaida() != null ? t.getSaida().toString() : null,
                        t.getDestino(),
                        t.getMotorista(),
                        t.getObservacao()
                )).toList();

        var agenda = agendaRepo.findByShowId(showId).stream()
                .map(a -> new AgendaDTO(
                        a.getId(),
                        a.getTitulo(),
                        a.getDescricao(),
                        a.getDataHoraInicio() != null ? a.getDataHoraInicio().toString() : null,
                        a.getDataHoraFim() != null ? a.getDataHoraFim().toString() : null,
                        a.getOrdem(),
                        a.getTipo() != null ? a.getTipo().getValue() : null
                )).toList();

        return new LogisticaEventoResponseDTO(hotels, voos, transportes, agenda);
    }
}
