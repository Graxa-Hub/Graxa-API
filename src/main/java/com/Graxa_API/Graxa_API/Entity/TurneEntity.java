package com.Graxa_API.Graxa_API.Entity;

import com.Graxa_API.Graxa_API.Entity.Evento.EventoEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TurneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeTurne;

    private LocalDateTime dataHoraInicioTurne;
    private LocalDateTime dataHoraFimTurne;

    @OneToMany(mappedBy = "turne", cascade = CascadeType.ALL)
    private List<EventoEntity> eventos;
    private boolean ativo = true;
    // Construtor vazio obrigatório para JPA
    public TurneEntity() {}

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public String getNomeTurne() {
        return nomeTurne;
    }

    public void setNomeTurne(String nomeTurne) {
        this.nomeTurne = nomeTurne;
    }

    public List<EventoEntity> getEventos() {
        return eventos;
    }

    public void setEventos(List<EventoEntity> eventos) {
        this.eventos = eventos;
    }

    public LocalDateTime getDataHoraInicioTurne() {
        return dataHoraInicioTurne;
    }

    public void setDataHoraInicioTurne(LocalDateTime dataHoraInicioTurne) {
        this.dataHoraInicioTurne = dataHoraInicioTurne;
    }

    public LocalDateTime getDataHoraFimTurne() {
        return dataHoraFimTurne;
    }

    public void setDataHoraFimTurne(LocalDateTime dataHoraFimTurne) {
        this.dataHoraFimTurne = dataHoraFimTurne;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
