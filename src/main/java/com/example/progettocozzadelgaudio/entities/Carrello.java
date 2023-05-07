package com.example.progettocozzadelgaudio.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Carrello {
    @GeneratedValue
    @Id
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @OneToOne(mappedBy = "carrello", optional = false)
    @JsonIgnore
    private Farmacia farmacia;

    public Farmacia getFarmacia() {
        return farmacia;
    }

    public void setFarmacia(Farmacia farmacia) {
        this.farmacia = farmacia;
    }

    @OneToMany
    private Collection<DettaglioCarrello> dettaglioCarrello = new ArrayList<>();

    public Collection<DettaglioCarrello> getDettaglioCarrello() {
        return dettaglioCarrello;
    }

    public void setDettaglioCarrello(Collection<DettaglioCarrello> dettaglioCarrello) {
        this.dettaglioCarrello = dettaglioCarrello;
    }
}
