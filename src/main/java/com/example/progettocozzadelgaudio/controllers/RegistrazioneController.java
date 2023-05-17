package com.example.progettocozzadelgaudio.controllers;

import com.example.progettocozzadelgaudio.entities.Cliente;
import com.example.progettocozzadelgaudio.entities.Farmacia;
import com.example.progettocozzadelgaudio.services.RegistrazioneService;
import com.example.progettocozzadelgaudio.support.exception.CFClienteGiaEsistenteException;
import com.example.progettocozzadelgaudio.support.exception.GestoreGiaEsistenteException;
import com.example.progettocozzadelgaudio.support.exception.PivaFarmaciaGiaEsistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/Registrazione")
public class RegistrazioneController {

    @Autowired
    RegistrazioneService registrazioneService;
    @PostMapping("/farmacia")
    public ResponseEntity registraFarmacia(@RequestBody @Valid String nome, @RequestBody @Valid String indirizzo,
                                           @RequestBody @Valid double budget, @RequestBody @Valid String citta,
                                           @RequestBody @Valid String partitaIva, @RequestBody @Valid String password ){
        try{
            Farmacia farmacia=registrazioneService.registraFarmacia(nome,indirizzo,budget,citta,partitaIva,password);
            return new ResponseEntity<>(farmacia,HttpStatus.OK);
        }catch(PivaFarmaciaGiaEsistenteException e){
            return new ResponseEntity<>("ERROR_MAIL_PHARMACY_ALREADY_EXISTS", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/cliente")
    public ResponseEntity registraCliente(@RequestBody @Valid String nome,@RequestBody @Valid String cognome, @RequestBody @Valid  String codiceFiscale,
                                          @RequestBody @Valid int giornoNascita, @RequestBody @Valid int meseNascita,
                                          @RequestBody @Valid int annoNascita, @RequestBody @Valid String citta,
                                          @RequestBody @Valid String indirizzo, @RequestBody @Valid  String password ){
        try{
            Cliente cliente=registrazioneService.registraCliente(nome,cognome,codiceFiscale,giornoNascita,meseNascita,annoNascita,citta,indirizzo,password);
            return new ResponseEntity<>(cliente,HttpStatus.OK);
        }catch(CFClienteGiaEsistenteException e){
            return new ResponseEntity<>("ERROR_MAIL_USER_ALREADY_EXISTS", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/gestore")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity registraGestre(@RequestBody @Valid String nome, @RequestBody @Valid String email,
                                         @RequestBody @Valid String password) {
        try {
            registrazioneService.registraGestore(nome,email,password);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(GestoreGiaEsistenteException e) {
            return new ResponseEntity<>("ERROR_MANAGER_ALREADY_EXISTS", HttpStatus.BAD_REQUEST);
        }
    }
}
