package com.example.progettocozzadelgaudio.controllers;

import com.example.progettocozzadelgaudio.authentication.Utils;
import com.example.progettocozzadelgaudio.entities.Carrello;
import com.example.progettocozzadelgaudio.services.AcquistoService;
import com.example.progettocozzadelgaudio.services.RegistrazioneService;
import com.example.progettocozzadelgaudio.support.exception.BudgetInsufficienteException;
import com.example.progettocozzadelgaudio.support.exception.PivaFarmaciaGiaEsistenteException;
import com.example.progettocozzadelgaudio.support.exception.QuantitaInsufficienteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.progettocozzadelgaudio.entities.Farmacia;

import jakarta.persistence.OptimisticLockException;

@RestController
public class PrimoController {
    @Autowired
    private RegistrazioneService registrazioneService;

    @Autowired
    private AcquistoService acquistoService;

   /* @GetMapping("/")
    public Farmacia addFarmacia(String nome,String indirizzo,Double budget, String citta,String partitaIva, String password) throws PivaFarmaciaGiaEsistenteException {
        return registrazioneService.registraFarmacia("ciao","ciao",0.2,"ciao","ciao","ciao");
    }*/

    @GetMapping("/ciao")
    public String ciao() {
        try {
            acquistoService.acquista();
        }catch(QuantitaInsufficienteException  | BudgetInsufficienteException | OptimisticLockException e3) { return "errore"; }
        return "ciao";
    }


    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/simple")
    public ResponseEntity check(){
        return new ResponseEntity("CKECK SOLO ADMIN", HttpStatus.OK);
    }

    @GetMapping("/prova2")
    @PreAuthorize("hasAnyAuthority('admin','prova')")
    public ResponseEntity check4(){
        return ResponseEntity.ok("SIA ADMIN CHE UTENTI PROVA "+
                "\n"+ Utils.getPrincipal().getClaims());
    }
}
