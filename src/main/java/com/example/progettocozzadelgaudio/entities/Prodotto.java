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
    private double prezzo_unitario;

    public double getPrezzo_unitario() {
        return prezzo_unitario;
    }

    public void setPrezzo_unitario(double prezzo_unitario) {
        this.prezzo_unitario = prezzo_unitario;
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
    private Integer qta_inStock;

    public Integer getQta_inStock() {
        return qta_inStock;
    }

    public void setQta_inStock(Integer qta_inStock) {
        this.qta_inStock = qta_inStock;
    }
}
