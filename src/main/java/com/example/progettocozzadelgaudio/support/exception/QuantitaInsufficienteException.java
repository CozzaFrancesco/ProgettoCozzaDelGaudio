package com.example.progettocozzadelgaudio.support.exception;


public class QuantitaInsufficienteException extends Exception{

    private Long idProdotto;
    public QuantitaInsufficienteException(Long idProdotto){
        this.idProdotto=idProdotto;
    }

    public Long getIdProdotto(){
        return idProdotto;
    }

}
