package com.example.progettocozzadelgaudio.entities;

import jakarta.persistence.*;

@Entity
public class DettaglioCarrello {

    @Id
    @GeneratedValue
    private Long id;


    @Basic
    private int quantita;

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    @ManyToOne(optional = false)
    private Prodotto prodotto;

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }
}
