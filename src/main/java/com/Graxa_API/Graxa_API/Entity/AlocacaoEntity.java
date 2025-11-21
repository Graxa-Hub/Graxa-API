package com.Graxa_API.Graxa_API.Entity;

import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Enums.StatusAlocacao;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AlocacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private ShowEntity show;

    @ManyToOne
    @JoinColumn(name = "colaborador_id", nullable = false)
    private ColaboradorEntity colaborador;

    @Enumerated(EnumType.STRING)
    private StatusAlocacao status = StatusAlocacao.PENDENTE;

    private boolean ativo = true;

    private LocalDateTime dataHoraCriacao;   // quando a alocação foi feita
    private LocalDateTime dataHoraResposta;  // quando colaborador aceitou/recusou

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public StatusAlocacao getStatus() {
        return status;
    }

    public void setStatus(StatusAlocacao status) {
        this.status = status;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataHoraCriacao() {
        return dataHoraCriacao;
    }

    public void setDataHoraCriacao(LocalDateTime dataHoraCriacao) {
        this.dataHoraCriacao = dataHoraCriacao;
    }

    public LocalDateTime getDataHoraResposta() {
        return dataHoraResposta;
    }

    public void setDataHoraResposta(LocalDateTime dataHoraResposta) {
        this.dataHoraResposta = dataHoraResposta;
    }
}
