package com.Graxa_API.Graxa_API.Entity.Evento;

import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "hotel_evento")
public class HotelEventoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Referência ao show
    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private ShowEntity show;

    // 🔗 Quem ficará hospedado
    @ManyToOne
    @JoinColumn(name = "colaborador_id", nullable = false)
    private ColaboradorEntity colaborador;

    // 🔗 Dados do hotel
    private String nomeHotel;

    private String endereco;

    private Double latitude;
    private Double longitude;

    private Double distanciaPalcoKm;
    private Double distanciaAeroportoKm;

    private LocalDateTime checkin;
    private LocalDateTime checkout;

    public HotelEventoEntity() {}

    // Getters e Setters
    public Long getId() { return id; }

    public ShowEntity getShow() { return show; }
    public void setShow(ShowEntity show) { this.show = show; }

    public ColaboradorEntity getColaborador() { return colaborador; }
    public void setColaborador(ColaboradorEntity colaborador) { this.colaborador = colaborador; }

    public String getNomeHotel() { return nomeHotel; }
    public void setNomeHotel(String nomeHotel) { this.nomeHotel = nomeHotel; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Double getDistanciaPalcoKm() { return distanciaPalcoKm; }
    public void setDistanciaPalcoKm(Double distanciaPalcoKm) { this.distanciaPalcoKm = distanciaPalcoKm; }

    public Double getDistanciaAeroportoKm() { return distanciaAeroportoKm; }
    public void setDistanciaAeroportoKm(Double distanciaAeroportoKm) { this.distanciaAeroportoKm = distanciaAeroportoKm; }

    public LocalDateTime getCheckin() { return checkin; }
    public void setCheckin(LocalDateTime checkin) { this.checkin = checkin; }

    public LocalDateTime getCheckout() { return checkout; }
    public void setCheckout(LocalDateTime checkout) { this.checkout = checkout; }
}
