package com.Graxa_API.Graxa_API.Factory;

import com.Graxa_API.Graxa_API.Entity.CredenciaisUsuarioEntity;
import com.Graxa_API.Graxa_API.Entity.TelefoneEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ArtistaEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.dto.ArtistaDto.RequestArtistaDto;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.RequestUsuarioDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioFactory {

    private final PasswordEncoder passwordEncoder;

    public UsuarioFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

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

        // Credenciais
        CredenciaisUsuarioEntity credenciais = new CredenciaisUsuarioEntity();
        credenciais.setNomeUsuario(dto.nomeUsuario());
        credenciais.setEmail(dto.email());
        credenciais.setSenha(passwordEncoder.encode(dto.senha()));
        credenciais.setUsuario(colaborador);
        colaborador.setCredenciais(credenciais);

        // Telefone
        if (dto.telefone() != null) {
            TelefoneEntity telefone = new TelefoneEntity();
            telefone.setTipoTelefone(dto.telefone().tipoTelefone());
            telefone.setNumeroTelefone(dto.telefone().numeroTelefone());
            telefone.setUsuario(colaborador);
            colaborador.setTelefones(List.of(telefone));
        }

        return colaborador;
    }
}
