package com.Graxa_API.Graxa_API.Entity.Evento;

import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "agenda_evento")
public class AgendaEventoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referência ao show
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private ShowEntity show;

    // Opcional: item pode ter um colaborador responsável/associado
    @ManyToOne
    @JoinColumn(name = "colaborador_id", nullable = true)
    private ColaboradorEntity colaborador;

    // Dados da agenda
    private String titulo;
    @Column(columnDefinition = "TEXT")
    private String descricao;

    // Data e hora de início do item
    private LocalDateTime dataHora;

    // Duração em minutos (opcional)
    private Integer duracaoMinutos;

    // Ordem para exibição (opcional)
    private Integer ordem;

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

    public ColaboradorEntity getColaborador() {
        return colaborador;
    }

    public void setColaborador(ColaboradorEntity colaborador) {
        this.colaborador = colaborador;
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

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Integer getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
}
