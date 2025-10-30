package com.Graxa_API.Graxa_API.Entity.Evento;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import com.Graxa_API.Graxa_API.Entity.LocalEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ShowEntity extends EventoEntity {

    @ManyToOne
    @JoinColumn(name = "local_id", nullable = false)
    private LocalEntity local;

    @ManyToMany
    @JoinTable(
            name = "show_banda",
            joinColumns = @JoinColumn(name = "show_id"),
            inverseJoinColumns = @JoinColumn(name = "banda_id")
    )
    private List<BandaEntity> bandas = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private ColaboradorEntity responsavelEvento;






    // Construtor vazio obrigatório para JPA
    public ShowEntity() {

    }

    // Getters e Setters
    public LocalEntity getLocal() {
        return local;
    }

    public void setLocal(LocalEntity local) {
        this.local = local;
    }

    public List<BandaEntity> getBandas() {
        return bandas;
    }

    public void setBandas(List<BandaEntity> bandas) {
        this.bandas = bandas;
    }

    public ColaboradorEntity getResponsavelEvento() {
        return responsavelEvento;
    }

    public void setResponsavelEvento(ColaboradorEntity responsavelEvento) {
        this.responsavelEvento = responsavelEvento;
    }

}
