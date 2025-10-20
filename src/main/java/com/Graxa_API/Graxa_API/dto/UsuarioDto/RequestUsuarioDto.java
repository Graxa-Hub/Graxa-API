package com.Graxa_API.Graxa_API.dto.UsuarioDto;

import com.Graxa_API.Graxa_API.Enums.TipoUsuario;
import com.Graxa_API.Graxa_API.dto.TelefoneDto.RequestTelefoneDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record RequestUsuarioDto(
        @NotBlank(message = "Preencha o nome")
        String nome,

        @Past(message = "Data de nascimento inválida")
        LocalDate dataNascimento,

        @CPF(message = "CPF inválido")
        String cpf,

        TipoUsuario tipoUsuario,

        @NotBlank(message = "O nome de usuário é obrigatório")
        String nomeUsuario,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
        String senha,

        @NotNull(message = "Telefone obrigatório")
        RequestTelefoneDto telefone
) {}
