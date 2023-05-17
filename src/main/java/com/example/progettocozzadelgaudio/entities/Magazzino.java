package com.example.progettocozzadelgaudio.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Magazzino {
    @GeneratedValue
    @Id
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @OneToMany(fetch = FetchType.EAGER)
    private Collection<DettaglioMagazzino> dettaglioMagazzino=new ArrayList<>();

    public Collection<DettaglioMagazzino> getDettaglioMagazzino() {
        return dettaglioMagazzino;
    }

    public void setDettaglioMagazzino(Collection<DettaglioMagazzino> dettaglioMagazzino) {
        this.dettaglioMagazzino = dettaglioMagazzino;
    }

    @OneToOne(mappedBy = "magazzino", optional = false)
    private Farmacia farmacia;

    public Farmacia getFarmacia() {
        return farmacia;
    }

    public void setFarmacia(Farmacia farmacia) {
        farmacia = farmacia;
    }
}
