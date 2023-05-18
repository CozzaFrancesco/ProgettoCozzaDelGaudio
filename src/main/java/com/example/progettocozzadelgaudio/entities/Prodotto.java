package com.example.progettocozzadelgaudio.entities;

import jakarta.persistence.*;

@Entity
public class Prodotto {
    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Version
    private long version;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
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
    @Column(name = "principio_attivo")
    private String principioAttivo;

    public String getPrincipioAttivo() {
        return principioAttivo;
    }

    public void setPrincipioAttivo(String principioAttivo) {
        this.principioAttivo = principioAttivo;
    }

    @Basic
    @Column(name="prezzo_unitario")
    private double prezzoUnitario;

    public double getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public void setPrezzoUnitario(double prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    @Basic
    @Column(name="forma_farmaceutica")
    private String formaFarmaceutica;

    public String getFormaFarmaceutica() {
        return formaFarmaceutica;
    }

    public void setFormaFarmaceutica(String formaFarmaceutica) {
        this.formaFarmaceutica = formaFarmaceutica;
    }

    @Basic
    @Column(name = "qta_inStock")
    private Integer qtaInStock;

    public Integer getQtaInStock() {
        return qtaInStock;
    }

    public void setQtaInStock(Integer qtaInStock) {
        this.qtaInStock = qtaInStock;
    }
}
