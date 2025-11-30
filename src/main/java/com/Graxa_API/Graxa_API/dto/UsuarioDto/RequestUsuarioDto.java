package com.Graxa_API.Graxa_API.dto.UsuarioDto;

import com.Graxa_API.Graxa_API.Enums.TipoUsuario;
import com.Graxa_API.Graxa_API.dto.TelefoneDto.RequestTelefoneDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Schema(description = "DTO para cadastro de usuário colaborador")
public record RequestUsuarioDto(

        @Schema(description = "Nome do arquivo da foto", example = "abc123.png")
        String fotoNome,

        @Schema(description = "Nome completo do usuário", example = "Gabriel Souza")
        @NotBlank(message = "Preencha o nome")
        String nome,

        @Schema(description = "Data de nascimento do usuário", example = "2000-05-15")
        @Past(message = "Data de nascimento inválida")
        LocalDate dataNascimento,

        @Schema(description = "CPF válido do usuário", example = "47060193898")
        @CPF(message = "CPF inválido")
        String cpf,

        @Schema(description = "Tipo de usuário", example = "PRODUTOR")
        TipoUsuario tipoUsuario,

        @Schema(description = "Nome de usuário para login", example = "gabrielsousa")
        @NotBlank(message = "O nome de usuário é obrigatório")
        String nomeUsuario,

        @Schema(description = "Email do usuário", example = "gabriel.sousa@example.com")
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @Schema(description = "Senha de acesso", example = "minhasenha123")
        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
        String senha,

        @Schema(description = "Telefone principal do usuário")
        @NotNull(message = "Telefone obrigatório")
        RequestTelefoneDto telefone


) {}
