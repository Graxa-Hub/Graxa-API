package com.Graxa_API.Graxa_API.dto.Logistica;

public record HotelDTO(
        Long id,
        Long colaboradorId,
        String nomeHotel,
        String endereco,
        Double latitude,
        Double longitude,
        Double distanciaPalcoKm,
        Double distanciaAeroportoKm,
        String checkin,
        String checkout
) {}
