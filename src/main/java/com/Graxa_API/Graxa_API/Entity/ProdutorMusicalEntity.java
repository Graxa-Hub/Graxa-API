package com.Graxa_API.Graxa_API.Entity;

import jakarta.persistence.Entity;

@Entity
public class ProdutorMusicalEntity extends UsuarioEntity {

//    private List<Banda> bandas;

    public ProdutorMusicalEntity() {
//        this.bandas = new ArrayList<>();
    }

    public ProdutorMusicalEntity(String nome, String email, String senha, String cpf, Boolean usuarioAtivo) {
        super(nome, email, senha, cpf, usuarioAtivo);
//        this.bandas = new ArrayList<>();
    }

//    public List<Banda> getBandas() {
//        return bandas;
//    }
//
//    public void setBandas(List<Banda> bandas) {
//        this.bandas = bandas;
//    }
}
