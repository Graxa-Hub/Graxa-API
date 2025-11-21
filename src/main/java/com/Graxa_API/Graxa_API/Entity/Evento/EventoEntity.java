package com.Graxa_API.Graxa_API.Entity.Evento;

import com.Graxa_API.Graxa_API.Entity.TurneEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class EventoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String nomeEvento;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String descricao;
    @ManyToOne
    @JoinColumn(name = "turne_id")
    private TurneEntity turne;
    @Column(nullable = false)
    private Boolean ativo = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TurneEntity getTurne() {
        return turne;
    }

    public void setTurne(TurneEntity turne) {
        this.turne = turne;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
