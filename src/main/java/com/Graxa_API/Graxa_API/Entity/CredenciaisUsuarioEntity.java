package com.Graxa_API.Graxa_API.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CredenciaisUsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, updatable = false)
    private UsuarioEntity usuario;

    private String nomeUsuario;

    @Column(unique = true)
    private String email;

    private String senha;

    private LocalDateTime dataHoraUltimoAcesso;

    protected CredenciaisUsuarioEntity() {}

    public CredenciaisUsuarioEntity(UsuarioEntity usuario, String nomeUsuario, String email, String senha) {
        this.usuario = usuario;
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senha = senha;
    }

    public Long getId() {
        return id;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
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

    public LocalDateTime getDataHoraUltimoAcesso() {
        return dataHoraUltimoAcesso;
    }

    public void setDataHoraUltimoAcesso(LocalDateTime dataHoraUltimoAcesso) {
        this.dataHoraUltimoAcesso = dataHoraUltimoAcesso;
    }
}
