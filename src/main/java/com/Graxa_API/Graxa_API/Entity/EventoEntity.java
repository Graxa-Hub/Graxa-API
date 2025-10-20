package com.Graxa_API.Graxa_API.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class EventoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeEvento;

     @ManyToOne
     @JoinColumn(name = "local_id")
     private LocalEntity local;

    @ManyToMany
    @JoinTable(
            name = "show_banda",
            joinColumns = @JoinColumn(name = "show_id"),
            inverseJoinColumns = @JoinColumn(name = "banda_id")
    )
    private List<BandaEntity> bandas;


    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity responsavelEvento;

    private LocalDateTime dataHoraEvento;
    @ManyToOne
    @JoinColumn(name = "turne_id", nullable = true) // permite que o campo seja nulo
    private TurneEntity turne;


    // @ManyToOne
    // @JoinColumn(name = "contratante_id")
    // private ContratanteEntity contratante;

    // Getters e Setters
    public Long getId() { return id; }

    public String getNomeEvento() { return nomeEvento; }
    public void setNomeEvento(String nomeEvento) { this.nomeEvento = nomeEvento; }

    public List<BandaEntity> getBandas() { return bandas; }
    public void setBandas(List<BandaEntity> bandas) { this.bandas = bandas; }

    public UsuarioEntity getResponsavelEvento() { return responsavelEvento; }
    public void setResponsavelEvento(UsuarioEntity responsavelEvento) { this.responsavelEvento = responsavelEvento; }

    public LocalDateTime getDataHoraEvento() { return dataHoraEvento; }
    public void setDataHoraEvento(LocalDateTime dataHoraEvento) { this.dataHoraEvento = dataHoraEvento; }

    // Getters e setters para local e contratante podem ser adicionados depois
}
