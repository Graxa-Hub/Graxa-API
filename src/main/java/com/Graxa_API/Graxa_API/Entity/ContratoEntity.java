package com.Graxa_API.Graxa_API.Entity;

import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ContratoEntity implements Identifiable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataUploadContrato;
    private String caminhoArquivoContrato;


    @OneToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private ShowEntity evento;


    public ContratoEntity() {}

    public Long getId() { return id; }

    public LocalDateTime getDataUploadContrato() { return dataUploadContrato; }
    public void setDataUploadContrato(LocalDateTime dataUploadContrato) { this.dataUploadContrato = dataUploadContrato; }

    public String getCaminhoArquivoContrato() { return caminhoArquivoContrato; }
    public void setCaminhoArquivoContrato(String caminhoArquivoContrato) { this.caminhoArquivoContrato = caminhoArquivoContrato; }

    public ShowEntity getEvento() { return evento; }
    public void setEvento(ShowEntity evento) { this.evento = evento; }
}
