package com.Graxa_API.Graxa_API.Entity.Usuario;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class RepresentanteEntity extends UsuarioEntity{
    @OneToMany(mappedBy = "representante")
    private List<BandaEntity> bandas = new ArrayList<>();
    @Column(unique = true)
    private String email;

    public List<BandaEntity> getBandas() {
        return bandas;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
