package com.Graxa_API.Graxa_API.Entity;

import com.Graxa_API.Graxa_API.Entity.Usuario.ArtistaEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.RepresentanteEntity;
import com.Graxa_API.Graxa_API.Enums.Genero;
import com.Graxa_API.Graxa_API.dto.BandaDto.RequestBandaDto;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class BandaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero")
    private Genero genero;

    @Column(name = "nome_foto")
    private String nomeFoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "representante_id", nullable = false)
    private RepresentanteEntity representante;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "banda_integrante",
            joinColumns = @JoinColumn(name = "banda_id"),
            inverseJoinColumns = @JoinColumn(name = "artista_id")
    )
    private List<ArtistaEntity> integrantes = new ArrayList<>();

    public BandaEntity() {}

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String getNomeFoto() {
        return nomeFoto;
    }

    public void setNomeFoto(String nomeFoto) {
        this.nomeFoto = nomeFoto;
    }

    public RepresentanteEntity getRepresentante() {
        return representante;
    }

    public void setRepresentante(RepresentanteEntity representante) {
        this.representante = representante;
    }

    public List<ArtistaEntity> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<ArtistaEntity> integrantes) {
        this.integrantes = integrantes;
    }
}