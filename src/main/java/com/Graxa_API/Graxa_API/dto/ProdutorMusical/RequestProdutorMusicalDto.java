package com.Graxa_API.Graxa_API.dto.ProdutorMusical;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;
import java.time.LocalDate;


public record RequestProdutorMusicalDto(
    @NotBlank(message = "Preencha o Nome")
     String nome,
    @Past(message = "Data de Nascimento Inválida")
     LocalDate dataNascimento,
    @Email(message = "O E-mail Deve Ser Valido!")
     String email,
    @Size(min=8, message = "A senha deve conter ao menos 8 Caracteres")
     String senha,
    @CPF(message = "CPF Inválido")
     String cpf,
     Boolean ativo
) { }
