package com.example.progettocozzadelgaudio.controllers;

import com.example.progettocozzadelgaudio.authentication.Utils;
import com.example.progettocozzadelgaudio.entities.Carrello;
import com.example.progettocozzadelgaudio.entities.DettaglioCarrello;
import com.example.progettocozzadelgaudio.services.AcquistoService;
import com.example.progettocozzadelgaudio.services.CarrelloService;
import com.example.progettocozzadelgaudio.support.exception.BudgetInsufficienteException;
import com.example.progettocozzadelgaudio.support.exception.QuantitaInsufficienteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.StringTokenizer;

@RestController
@RequestMapping("/carrello")
public class CarrelloController {

    @Autowired
    private CarrelloService carrelloService;

    @Autowired
    private AcquistoService acquistoService;

    @GetMapping
    @PreAuthorize("hasAuthority('farmacia')")
    public Collection<DettaglioCarrello> visualizzaCarrello() {
        return carrelloService.visualizzaCarrello();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('farmacia')")
    public ResponseEntity aggiungiAlCarrello(@Valid @RequestBody Long idProdotto,@Valid @RequestBody int quantita) {
        try{
            return new ResponseEntity(carrelloService.aggiungiAlCarrello(idProdotto,quantita),HttpStatus.OK);
        }catch (QuantitaInsufficienteException e){
            return new ResponseEntity("ERROR_QUANTITY_NOT_ENOUGH", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    @PreAuthorize("hasAuthority('farmacia')")
    public ResponseEntity modificaCarrello(@Valid @RequestBody Long idProdotto,@Valid @RequestBody int quantita) { //quantita in meno o in piu
        try{
            return new ResponseEntity(carrelloService.modificaCarrello(idProdotto,quantita),HttpStatus.OK);
        }catch (QuantitaInsufficienteException e){
            return new ResponseEntity("ERROR_QUANTITY_NOT_ENOUGH", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('farmacia')")
    public ResponseEntity eliminaProdottoInCarrello(@Valid @RequestBody Long idProdotto) {
        try{
            int quantitaPerEliminazione=0;
            return new ResponseEntity(carrelloService.modificaCarrello(idProdotto,quantitaPerEliminazione),HttpStatus.OK);
        }catch (QuantitaInsufficienteException e){
            return new ResponseEntity( HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/acquisto")
    @PreAuthorize("hasAuthority('farmacia')")
    public ResponseEntity acquistaCarrello(){
        try{
            return new ResponseEntity(acquistoService.acquista(), HttpStatus.OK);
        }catch(QuantitaInsufficienteException | BudgetInsufficienteException e ){
            return new ResponseEntity("ERROR_PURCHASE_FAILED",HttpStatus.BAD_REQUEST);
        }
    }
}
