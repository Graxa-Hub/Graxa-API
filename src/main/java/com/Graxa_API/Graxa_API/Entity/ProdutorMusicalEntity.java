package com.Graxa_API.Graxa_API.Entity;

import com.Graxa_API.Graxa_API.dto.ProdutorMusical.RequestProdutorMusicalDto;
import jakarta.persistence.Entity;

@Entity
public class ProdutorMusicalEntity extends UsuarioEntity {

    public ProdutorMusicalEntity() {
    }

    public ProdutorMusicalEntity(RequestProdutorMusicalDto produtorMusicaoDto) {
        super(produtorMusicaoDto.nome(), produtorMusicaoDto.dataNascimento() ,produtorMusicaoDto.email(), produtorMusicaoDto.cpf(), produtorMusicaoDto.senha(), produtorMusicaoDto.ativo());
    }
    public ProdutorMusicalEntity(Long id, RequestProdutorMusicalDto produtorMusicaoDto) {
        super(id, produtorMusicaoDto.nome(), produtorMusicaoDto.dataNascimento() ,produtorMusicaoDto.email(), produtorMusicaoDto.cpf(), produtorMusicaoDto.senha(), produtorMusicaoDto.ativo());
    }
}
