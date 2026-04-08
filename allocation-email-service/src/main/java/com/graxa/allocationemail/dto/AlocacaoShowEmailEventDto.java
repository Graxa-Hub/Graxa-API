package com.graxa.allocationemail.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AlocacaoShowEmailEventDto(
        @NotBlank String userId,
        @NotBlank @Email String email,
        @NotBlank String showId,
        @NotBlank String showName,
        @NotBlank String date
) {
}
