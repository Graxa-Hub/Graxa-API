package com.Graxa_API.Graxa_API.Entity.Evento;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class ExtraEventoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "show_id", nullable = false, unique = true)
    private ShowEntity show;

    @Column(columnDefinition = "TEXT")
    private String obs;

    @Column(columnDefinition = "TEXT")
    private String contatos;

    // Getters e Setters
    public Long getId() { return id; }

    public ShowEntity getShow() { return show; }
    public void setShow(ShowEntity show) { this.show = show; }

    public String getObs() { return obs; }
    public void setObs(String obs) { this.obs = obs; }

    public String getContatos() { return contatos; }
    public void setContatos(String contatos) { this.contatos = contatos; }
}
