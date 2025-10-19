package com.Graxa_API.Graxa_API.Entity;

import com.Graxa_API.Graxa_API.Enums.TipoAcao;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AcaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity responsavelAcao;

    @Enumerated(EnumType.STRING)
    private TipoAcao tipoAcao;

    private LocalDateTime dataHoraAcao;


    public Long getId() {
        return id;
    }

    public UsuarioEntity getResponsavelAcao() {
        return responsavelAcao;
    }

    public void setResponsavelAcao(UsuarioEntity responsavelAcao) {
        this.responsavelAcao = responsavelAcao;
    }

    public TipoAcao getTipoAcao() {
        return tipoAcao;
    }

    public void setTipoAcao(TipoAcao tipoAcao) {
        this.tipoAcao = tipoAcao;
    }

    public LocalDateTime getDataHoraAcao() {
        return dataHoraAcao;
    }

    public void setDataHoraAcao(LocalDateTime dataHoraAcao) {
        this.dataHoraAcao = dataHoraAcao;
    }
}
