package com.example.progettocozzadelgaudio.entities;

import jakarta.persistence.*;

@Entity
public class Farmacia {
    @GeneratedValue
    @Id
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        nome = nome;
    }

    @Basic
    private String indirizzo;

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        indirizzo = indirizzo;
    }

    @OneToOne(optional = true)
    private Magazzino magazzino;

    public Magazzino getMagazzino() {
        return magazzino;
    }

    public void setMagazzino(Magazzino magazzino) {
        magazzino = magazzino;
    }

    @Basic
    private Double budget;

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    @Basic
    private String citta;

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    @OneToOne
    private Carrello carrello;

    public Carrello getCarrello() {
        return carrello;
    }

    public void setCarrello(Carrello carrello) {
        this.carrello = carrello;
    }

    @Basic
    private String PartitaIva;

    public String getPartitaIva() {
        return PartitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        PartitaIva = partitaIva;
    }
}
