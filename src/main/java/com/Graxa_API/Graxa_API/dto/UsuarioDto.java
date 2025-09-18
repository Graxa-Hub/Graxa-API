package com.Graxa_API.Graxa_API.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UsuarioDto() {
    @NotBlank(message = "Preencha o Nome")
    static String nome;
    @Past(message = "Data de Nascimento Inválida")
    static LocalDate dataNascimento;
    @Email(message = "O E-mail Deve Ser Valido!")
    static String email;
    @Size(min=8, message = "A senha deve conter ao menos 8 Caracteres")
    static String senha;
    @CPF(message = "CPF Inválido")
    static String cpf;
     static Boolean ativo;
     
}
