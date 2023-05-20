package com.example.progettocozzadelgaudio.entities;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

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
        this.nome = nome;
    }

    @Basic
    private String indirizzo;

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    @OneToOne
    private Magazzino magazzino;

    public Magazzino getMagazzino() {
        return magazzino;
    }

    public void setMagazzino(Magazzino magazzino) {
        this.magazzino = magazzino;
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
    @Column(name="partita_iva")
    private String partitaIva;

    public String getPartitaIva() {
        return partitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    @Basic
    private Integer numDipendenti;

    public Integer getNumDipendenti() {
        return numDipendenti;
    }

    public void setNumDipendenti(Integer numDipendenti) {
        this.numDipendenti = numDipendenti;
    }

    @Basic
    private LocalTime orarioInizioVisite;

    public LocalTime getOrarioInizioVisite() {
        return orarioInizioVisite;
    }

    public void setOrarioInizioVisite(LocalTime orarioInizioVisite) {
        this.orarioInizioVisite = orarioInizioVisite;
    }

    @Basic
    private LocalTime orarioFineVisite;

    public LocalTime getOrarioFineVisite() {
        return orarioFineVisite;
    }

    public void setOrarioFineVisite(LocalTime orarioFineVisite) {
        this.orarioFineVisite = orarioFineVisite;
    }

    @ManyToMany
    private Collection<Visita> visite=new ArrayList<>();

    public Collection<Visita> getVisite() {
        return visite;
    }

    public void setVisite(Collection<Visita> visite) {
        this.visite = visite;
    }
}
