package com.Graxa_API.Graxa_API.Model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private Boolean usuarioAtivo;
    public Usuario() {
    }

    public Usuario(String nome, String email, String senha, String cpf, Boolean usuarioAtivo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.usuarioAtivo = usuarioAtivo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getStatusUsuarioAtivo() {
        return usuarioAtivo;
    }

    public void setStatusUsuarioAtivo(Boolean statusUsuario) {
        this.usuarioAtivo = statusUsuario;
    }
}
