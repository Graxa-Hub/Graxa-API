package com.Graxa_API.Graxa_API.Entity.Usuario;

import com.Graxa_API.Graxa_API.Entity.BandaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity
public class ArtistaEntity extends UsuarioEntity {
    @ManyToMany(mappedBy = "integrantes")
    private List<BandaEntity> bandas;
}
