package com.Graxa_API.Graxa_API.Model;

import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ProdutorMusical extends Usuario{

//    private List<Banda> bandas;

    public ProdutorMusical() {
//        this.bandas = new ArrayList<>();
    }

    public ProdutorMusical(String nome, String email, String senha, String cpf, Boolean usuarioAtivo) {
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
