package com.Graxa_API.Graxa_API.Entity.Evento;

import com.Graxa_API.Graxa_API.Enums.TipoViagem;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class ViagemEntity extends EventoEntity{
    @Enumerated(EnumType.STRING)
    TipoViagem tipoViagem;

    public TipoViagem getTipoViagem() {
        return tipoViagem;
    }

    public void setTipoViagem(TipoViagem tipoViagem) {
        this.tipoViagem = tipoViagem;
    }
}
