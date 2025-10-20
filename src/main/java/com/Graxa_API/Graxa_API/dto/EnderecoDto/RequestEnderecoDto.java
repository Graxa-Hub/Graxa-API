package com.Graxa_API.Graxa_API.dto.EnderecoDto;

import com.Graxa_API.Graxa_API.Enums.TipoEndereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RequestEnderecoDto(
        @NotNull(message = "O tipo de endereço é obrigatório")
        TipoEndereco tipoEndereco,

        @NotBlank(message = "O CEP é obrigatório")
        String cep,

        @NotBlank(message = "O logradouro é obrigatório")
        String logradouro,

        @NotBlank(message = "O bairro é obrigatório")
        String bairro,
        @NotNull(message = "O número é obrigatório")
        @Positive(message = "O número deve ser positivo")
        Integer numero,

        String complemento,

        @NotBlank(message = "A cidade é obrigatória")
        String cidade,

        @NotBlank(message = "O estado é obrigatório")
        String estado,

        @NotBlank(message = "O país é obrigatório")
        String pais
) {}
