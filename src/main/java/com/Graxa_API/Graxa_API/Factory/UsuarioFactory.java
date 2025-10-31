package com.Graxa_API.Graxa_API.Factory;

import com.Graxa_API.Graxa_API.Entity.Usuario.ArtistaEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.dto.ArtistaDto.RequestArtistaDto;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.RequestUsuarioDto;
import org.springframework.stereotype.Component;

@Component
public class UsuarioFactory {

    public Object criarUsuario(Object dto) {
        if (dto instanceof RequestArtistaDto artistaDto) {
            return criarArtista(artistaDto);
        } else if (dto instanceof RequestUsuarioDto colaboradorDto) {
            return criarColaborador(colaboradorDto);
        } else {
            throw new IllegalArgumentException("Tipo de DTO desconhecido");
        }
    }

    private ArtistaEntity criarArtista(RequestArtistaDto dto) {
        ArtistaEntity artista = new ArtistaEntity();
        artista.setNome(dto.nome());
        artista.setCpf(dto.cpf());
        artista.setFotoNome(dto.fotoNome());
        artista.setAtivo(true);
        return artista;
    }

    private ColaboradorEntity criarColaborador(RequestUsuarioDto dto) {
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setNome(dto.nome());
        colaborador.setCpf(dto.cpf());
        colaborador.setDataNascimento(dto.dataNascimento());
        colaborador.setTipoUsuario(dto.tipoUsuario());
        colaborador.setAtivo(true);
        return colaborador;
    }
}
