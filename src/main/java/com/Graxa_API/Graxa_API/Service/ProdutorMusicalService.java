package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.dto.UsuarioDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {
    private final List<UsuarioDto> usuarios = new ArrayList();
    private Long proximoId = 1L;

    public List<UsuarioDto>listarUsuarios(){
        return usuarios;
    }


    public UsuarioDto adicionarUsuario(String nome, String cpf, String email, Boolean ativo){
        UsuarioDto novoUsuario = new UsuarioDto(nome, email,cpf, ativo);
    }
}