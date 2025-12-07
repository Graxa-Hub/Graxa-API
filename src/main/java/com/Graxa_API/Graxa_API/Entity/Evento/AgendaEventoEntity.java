package com.Graxa_API.Graxa_API.Entity.Evento;

import com.Graxa_API.Graxa_API.Enums.TipoAgendaItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "agenda_evento")
public class AgendaEventoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private ShowEntity show;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;

    private Integer ordem;

    @Enumerated(EnumType.STRING)
    private TipoAgendaItem tipo;

    public AgendaEventoEntity() {}

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public ShowEntity getShow() {
        return show;
    }

    public void setShow(ShowEntity show) {
        this.show = show;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public TipoAgendaItem getTipo() {
        return tipo;
    }

    public void setTipo(TipoAgendaItem tipo) {
        this.tipo = tipo;
    }
}
