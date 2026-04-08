package com.Graxa_API.Graxa_API.dto.AlocacaoDto;

public record AlocacaoShowEmailEventDto(
        String userId,
        String email,
        String showId,
        String showName,
        String date
) {
}
