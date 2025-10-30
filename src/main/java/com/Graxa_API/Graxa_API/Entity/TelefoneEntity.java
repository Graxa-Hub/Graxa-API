package com.Graxa_API.Graxa_API.Entity;

import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Enums.TipoTelefone;
import jakarta.persistence.*;
@Entity
public class TelefoneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private ColaboradorEntity usuario;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_telefone")
    private TipoTelefone tipoTelefone;
    private String numeroTelefone;

    public Long getId() {
        return id;
    }

    public ColaboradorEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(ColaboradorEntity usuario) {
        this.usuario = usuario;
    }

    public TipoTelefone getTipoTelefone() {
        return tipoTelefone;
    }

    public void setTipoTelefone(TipoTelefone tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }

    public String getNumeroTelefone() {
        return numeroTelefone;
    }

    public void setNumeroTelefone(String numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }
}
