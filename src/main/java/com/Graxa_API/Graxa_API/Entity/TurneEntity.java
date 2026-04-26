package com.Graxa_API.Graxa_API.Entity;

import com.Graxa_API.Graxa_API.Entity.Evento.EventoEntity;
import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TurneEntity implements Identifiable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeTurne;
    private LocalDateTime dataHoraInicioTurne;
    private LocalDateTime dataHoraFimTurne;

    @OneToMany(mappedBy = "turne", cascade = CascadeType.ALL)
    private List<ShowEntity> eventos;

    private boolean ativo = true;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "banda_id", nullable = false)
    private BandaEntity banda;
    @ManyToOne
    @JoinColumn(name = "criador_id", nullable = false)
    private ColaboradorEntity criadoPor;


    private String nomeImagem;

    // Novo campo
    private String descricao;

    public TurneEntity() {}

    // Getters e Setters
    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeTurne() { return nomeTurne; }
    public void setNomeTurne(String nomeTurne) { this.nomeTurne = nomeTurne; }

    public LocalDateTime getDataHoraInicioTurne() { return dataHoraInicioTurne; }
    public void setDataHoraInicioTurne(LocalDateTime dataHoraInicioTurne) { this.dataHoraInicioTurne = dataHoraInicioTurne; }

    public LocalDateTime getDataHoraFimTurne() { return dataHoraFimTurne; }
    public void setDataHoraFimTurne(LocalDateTime dataHoraFimTurne) { this.dataHoraFimTurne = dataHoraFimTurne; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public String getNomeImagem() { return nomeImagem; }
    public void setNomeImagem(String nomeImagem) { this.nomeImagem = nomeImagem; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BandaEntity getBanda() {
        return banda;
    }

    public void setBanda(BandaEntity banda) {
        this.banda = banda;
    }

    public List<ShowEntity> getEventos() {
        return eventos;
    }

    public void setEventos(List<ShowEntity> eventos) {
        this.eventos = eventos;
    }

    public ColaboradorEntity getCriadoPor() { return criadoPor; }
    public void setCriadoPor(ColaboradorEntity criadoPor) { this.criadoPor = criadoPor; }
}

