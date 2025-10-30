package com.Graxa_API.Graxa_API.Entity.Usuario;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean ativo = true;

    private String nome;

    @Column(unique = true)
    private String cpf;

    // Getters e Setters
    public Long getId() { return id; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
}
