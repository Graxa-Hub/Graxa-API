package com.Graxa_API.Graxa_API.Entity;

import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class NotificacaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // ✅ MUDANÇA: LAZY para EAGER
    @JoinColumn(name = "colaborador_id", nullable = false)
    private ColaboradorEntity colaborador;

    // ✅ MUDANÇA: De LAZY para EAGER para carregar alocação automaticamente
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "alocacao_id")
    private AlocacaoEntity alocacao;

    @Column(nullable = false, length = 500)
    private String mensagem;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false)
    private boolean lida = false;

    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    // Getters e Setters (mantém todos iguais)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ColaboradorEntity getColaborador() {
        return colaborador;
    }

    public void setColaborador(ColaboradorEntity colaborador) {
        this.colaborador = colaborador;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public AlocacaoEntity getAlocacao() {
        return alocacao;
    }

    public void setAlocacao(AlocacaoEntity alocacao) {
        this.alocacao = alocacao;
    }
}