package com.example.progettocozzadelgaudio.controllers;

import com.example.progettocozzadelgaudio.services.RegistrazioneService;
import com.example.progettocozzadelgaudio.support.exception.PivaFarmaciaGiaEsistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.progettocozzadelgaudio.entities.Farmacia;

@RestController
public class PrimoController {
    @Autowired
    private RegistrazioneService registrazioneService;

    @GetMapping("/")
    public Farmacia addFarmacia(String nome,String indirizzo,Double budget, String citta,String partitaIva, String password) throws PivaFarmaciaGiaEsistenteException {
        return registrazioneService.registraFarmacia("ciao","ciao",0.2,"ciao","ciao","ciao");
    }
}
