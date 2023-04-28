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

    @Basic
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        nome = nome;
    }

    @Basic
    private String principio_attivo;

    public String getPrincipio_attivo() {
        return principio_attivo;
    }

    public void setPrincipio_attivo(String principioAttivo) {
        this.principio_attivo = principioAttivo;
    }

    @Basic
    private String prezzo_unitario;

    public String getPrezzo_unitario() {
        return prezzo_unitario;
    }

    public void setPrezzo_unitario(String prezzo_unitario) {
        this.prezzo_unitario = prezzo_unitario;
    }

    @Basic
    private String forma_farmaceutica;

    public String getForma_farmaceutica() {
        return forma_farmaceutica;
    }

    public void setForma_farmaceutica(String forma_farmaceutica) {
        this.forma_farmaceutica = forma_farmaceutica;
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
