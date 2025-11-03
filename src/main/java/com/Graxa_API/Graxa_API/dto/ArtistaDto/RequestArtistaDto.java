package com.Graxa_API.Graxa_API.dto.ArtistaDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para cadastro de artista")
public record RequestArtistaDto(

        @Schema(description = "Nome completo do artista", example = "Ana Beatriz")
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
        String nome,

        @Schema(description = "CPF do artista com 11 dígitos numéricos", example = "12345678901")
        @NotBlank(message = "O CPF é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos numéricos")
        String cpf,

        @Schema(description = "Nome do arquivo da foto do artista", example = "foto_ana_beatriz.jpg")
        @Size(max = 150, message = "O nome da foto deve ter no máximo 150 caracteres")
        String fotoNome

) {}
