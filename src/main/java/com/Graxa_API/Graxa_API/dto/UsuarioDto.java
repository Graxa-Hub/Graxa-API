package com.Graxa_API.Graxa_API.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public class UsuarioDto {
    @NotBlank(message = "Preencha o Nome")
    private String nome;
    @Past(message = "Data de Nascimento Inválida")
    private LocalDate dataNascimento;
    @Email(message = "O E-mail Deve Ser Valido!")
    private String email;
    @Size(min=8, message = "A senha deve conter ao menos 8 Caracteres")
    private String senha;
    @CPF(message = "CPF Inválido")
    private String cpf;
    private Boolean ativo;



    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
