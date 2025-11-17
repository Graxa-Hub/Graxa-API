package com.Graxa_API.Graxa_API.Entity;

import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CredenciaisUsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, updatable = false)
    private ColaboradorEntity usuario;

    private String nomeUsuario;

    @Column(unique = true)
    private String email;

    private String senha;

    private LocalDateTime dataHoraUltimoAcesso;

    private String codigoRecuperacao;

    private LocalDateTime codigoExpiraEm;

    public CredenciaisUsuarioEntity() {}


    public Long getId() {
        return id;
    }

    public ColaboradorEntity getUsuario() {
        return usuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setUsuario(ColaboradorEntity usuario) {
        this.usuario = usuario;
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

    public String getCodigoRecuperacao() {
        return codigoRecuperacao;
    }

    public void setCodigoRecuperacao(String codigoRecuperacao) {
        this.codigoRecuperacao = codigoRecuperacao;
    }

    public LocalDateTime getCodigoExpiraEm() {
        return codigoExpiraEm;
    }

    public void setCodigoExpiraEm(LocalDateTime codigoExpiraEm) {
        this.codigoExpiraEm = codigoExpiraEm;
    }
}
