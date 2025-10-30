package com.Graxa_API.Graxa_API.dto.LocalDto;

import com.Graxa_API.Graxa_API.dto.EnderecoDto.RequestEnderecoDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestLocalDto(
        @NotBlank(message = "O nome do local é obrigatório")
        String nome,

        @NotNull(message = "O ID do endereço é obrigatório")
        Long idEndereco,

        @NotNull(message = "A capacidade é obrigatória")
        @Min(value = 1, message = "A capacidade deve ser maior que zero")
        Integer capacidade
) {}

