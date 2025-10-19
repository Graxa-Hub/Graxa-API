package com.Graxa_API.Graxa_API.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ContratoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataUploadContrato;
    private String caminhoArquivoContrato;


    @OneToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private EventoEntity evento;


    public ContratoEntity() {}

    public Long getId() { return id; }

    public LocalDateTime getDataUploadContrato() { return dataUploadContrato; }
    public void setDataUploadContrato(LocalDateTime dataUploadContrato) { this.dataUploadContrato = dataUploadContrato; }

    public String getCaminhoArquivoContrato() { return caminhoArquivoContrato; }
    public void setCaminhoArquivoContrato(String caminhoArquivoContrato) { this.caminhoArquivoContrato = caminhoArquivoContrato; }

    public EventoEntity getEvento() { return evento; }
    public void setEvento(EventoEntity evento) { this.evento = evento; }
}
