package com.example.progettocozzadelgaudio.entities;

import jakarta.persistence.*;

@Entity
public class DettaglioMagazzino {
    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @OneToOne(optional = false)
    private Prodotto Prodotto;

    public Prodotto getProdotto() {
        return Prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        Prodotto = prodotto;
    }

    @Basic
    private int quantita;

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    private String hello;
}
