package com.Graxa_API.Graxa_API.dto.Hotel;

import java.time.LocalDateTime;

public class HotelEventoCreateDTO {

    private Long showId;
    private Long colaboradorId;

    private String nomeHotel;
    private String endereco;

    private Double latitude;
    private Double longitude;

    private Double distanciaPalcoKm;
    private Double distanciaAeroportoKm;

    private LocalDateTime checkin;
    private LocalDateTime checkout;

    // getters e setters


    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public Long getColaboradorId() {
        return colaboradorId;
    }

    public void setColaboradorId(Long colaboradorId) {
        this.colaboradorId = colaboradorId;
    }

    public String getNomeHotel() {
        return nomeHotel;
    }

    public void setNomeHotel(String nomeHotel) {
        this.nomeHotel = nomeHotel;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getDistanciaPalcoKm() {
        return distanciaPalcoKm;
    }

    public void setDistanciaPalcoKm(Double distanciaPalcoKm) {
        this.distanciaPalcoKm = distanciaPalcoKm;
    }

    public Double getDistanciaAeroportoKm() {
        return distanciaAeroportoKm;
    }

    public void setDistanciaAeroportoKm(Double distanciaAeroportoKm) {
        this.distanciaAeroportoKm = distanciaAeroportoKm;
    }

    public LocalDateTime getCheckin() {
        return checkin;
    }

    public void setCheckin(LocalDateTime checkin) {
        this.checkin = checkin;
    }

    public LocalDateTime getCheckout() {
        return checkout;
    }

    public void setCheckout(LocalDateTime checkout) {
        this.checkout = checkout;
    }
}
