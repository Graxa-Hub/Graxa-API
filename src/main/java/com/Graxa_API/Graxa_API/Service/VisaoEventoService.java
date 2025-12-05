package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.dto.VisaoEventoDto.VisaoEventoDto;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VisaoEventoService {

    public VisaoEventoDto montarVisaoEvento(ShowEntity show) {

        String artista = show.getTurne().getBanda().getNome();

        String turne = show.getTurne().getNomeTurne();

        String dataInfo = show.getDataInicio().toLocalDate()
                + " — " + show.getLocal().getEndereco().getCidade();

        var coords = new VisaoEventoDto.Coords(
                -23.5505,  // MOCKADO — trocar depois
                -46.6333
        );

        String cidade = show.getLocal().getEndereco().getCidade();

        Integer progresso = calcularProgresso(show.getDataInicio(), show.getDataFim());

        List<VisaoEventoDto.AgendaItem> agenda = agendaPadrao();

        return new VisaoEventoDto(
                artista,
                turne,
                dataInfo,
                coords,
                cidade,
                progresso,
                agenda
        );
    }

    private Integer calcularProgresso(LocalDateTime inicio, LocalDateTime fim) {
        LocalDateTime agora = LocalDateTime.now();

        if (agora.isBefore(inicio)) return 0;
        if (agora.isAfter(fim)) return 100;

        long total = Duration.between(inicio, fim).toMinutes();
        long atual = Duration.between(inicio, agora).toMinutes();

        return (int) ((atual * 100) / total);
    }

    private List<VisaoEventoDto.AgendaItem> agendaPadrao() {
        return List.of(
                new VisaoEventoDto.AgendaItem("12:00", "Transporte", "Van sai às 12h do hotel.", true),
                new VisaoEventoDto.AgendaItem("13:00", "Montagem", "Montagem do palco.", false),
                new VisaoEventoDto.AgendaItem("15:30", "Passagem de som", "Soundcheck com a banda.", false),
                new VisaoEventoDto.AgendaItem("18:00", "Showtime", "Entrada no palco.", false)
        );
    }
}
