package com.Graxa_API.Graxa_API.dto.EnderecoDto;

import com.Graxa_API.Graxa_API.Enums.TipoEndereco;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "DTO para criação ou atualização de endereço")
public record RequestEnderecoDto(

        @NotNull(message = "O tipo de endereço é obrigatório")
        @Schema(description = "Tipo do endereço (ex: RESIDENCIAL, COMERCIAL)", example = "local")
        TipoEndereco tipoEndereco,

        @NotBlank(message = "O CEP é obrigatório")
        @Schema(description = "Código de Endereçamento Postal", example = "08506-000")
        String cep,

        @NotBlank(message = "O logradouro é obrigatório")
        @Schema(description = "Nome da rua, avenida ou via", example = "Rua das Flores")
        String logradouro,

        @NotBlank(message = "O bairro é obrigatório")
        @Schema(description = "Nome do bairro", example = "Centro")
        String bairro,

        @NotNull(message = "O número é obrigatório")
        @Positive(message = "O número deve ser positivo")
        @Schema(description = "Número do imóvel", example = "123")
        Integer numero,

        @Schema(description = "Complemento do endereço", example = "Apartamento 45")
        String complemento,

        @NotBlank(message = "A cidade é obrigatória")
        @Schema(description = "Nome da cidade", example = "Ferraz de Vasconcelos")
        String cidade,

        @NotBlank(message = "O estado é obrigatório")
        @Schema(description = "Sigla do estado", example = "SP")
        String estado,

        @NotBlank(message = "O país é obrigatório")
        @Schema(description = "Nome do país", example = "Brasil")
        String pais
) {}
