package com.Graxa_API.Graxa_API.Factory;

import com.Graxa_API.Graxa_API.Entity.Evento.EventoEntity;
import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.Entity.Evento.ViagemEntity;
import com.Graxa_API.Graxa_API.Entity.LocalEntity;
import com.Graxa_API.Graxa_API.Entity.TurneEntity;
import com.Graxa_API.Graxa_API.Entity.UsuarioEntity;
import com.Graxa_API.Graxa_API.Repository.LocalRepository;
import com.Graxa_API.Graxa_API.Repository.TurneRepository;
import com.Graxa_API.Graxa_API.Repository.UsuarioRepository;
import com.Graxa_API.Graxa_API.dto.ShowDto.RequestShowDto;
import com.Graxa_API.Graxa_API.dto.ViagemDto.RequestViagemDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventoFactory {
    public EventoEntity criarEvento(Object dto) {
        if (dto instanceof RequestShowDto showDto) {
            return criarShow(showDto);
        } else if (dto instanceof RequestViagemDto viagemDto) {
            return criarViagem(viagemDto);
        } else {
            throw new IllegalArgumentException("Tipo de DTO desconhecido");
        }
    }

    @Autowired
    private TurneRepository turneRepository;

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private ShowEntity criarShow(RequestShowDto dto) {
        ShowEntity show = new ShowEntity();

        show.setNomeEvento(dto.nomeEvento());
        show.setDataInicio(dto.dataInicio());
        show.setDataFim(dto.dataFim());
        show.setDescricao(dto.descricao());


        TurneEntity turne = turneRepository
                .findById(dto.turneId())
                .orElseThrow(() -> new EntityNotFoundException("Turnê não encontrado"));
        show.setTurne(turne);

        LocalEntity local = localRepository
                .findById(dto.localId())
                .orElseThrow(() -> new EntityNotFoundException("Local não encontrado"));
        show.setLocal(local);

        UsuarioEntity usuario = usuarioRepository
                .findById(dto.responsavelId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        show.setResponsavelEvento(usuario);

        return show;
    }

    private ViagemEntity criarViagem(RequestViagemDto dto) {
        ViagemEntity viagem = new ViagemEntity();

        viagem.setNomeEvento(dto.nomeEvento());
        viagem.setDataInicio(dto.dataInicio());
        viagem.setDataFim(dto.dataFim());
        viagem.setDescricao(dto.descricao());
        TurneEntity turne = turneRepository
                .findById(dto.turneId())
                .orElseThrow(() -> new EntityNotFoundException("Turnê não encontrado"));
        viagem.setTurne(turne);
        viagem.setTipoViagem(dto.tipoViagem());

        return viagem;
    }




}
