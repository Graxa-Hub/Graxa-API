package com.Graxa_API.Graxa_API.dto.BandaDto;

import com.Graxa_API.Graxa_API.Enums.Genero;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.ResponseUsuarioDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record RequestBandaDto (
        @NotBlank String nome,
        @NotBlank String descricao,
        @NotBlank Genero genero
){}
