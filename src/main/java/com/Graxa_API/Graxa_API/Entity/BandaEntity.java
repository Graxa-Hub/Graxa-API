package com.Graxa_API.Graxa_API.Entity;

import com.Graxa_API.Graxa_API.Enums.Genero;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class BandaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private Genero genero;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "banda_integrante",
            joinColumns = @JoinColumn(name = "banda_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<UsuarioEntity> integrantes;

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

    public List<UsuarioEntity> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<UsuarioEntity> integrantes) {
        this.integrantes = integrantes;
    }
}
