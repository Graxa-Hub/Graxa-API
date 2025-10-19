package com.Graxa_API.Graxa_API.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TurneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeTurne;
    @OneToMany(mappedBy = "turne")
    private List<EventoEntity> eventos;



}
