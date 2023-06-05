package com.example.progettocozzadelgaudio.controllers;

import com.example.progettocozzadelgaudio.entities.Carrello;
import com.example.progettocozzadelgaudio.entities.Prodotto;
import com.example.progettocozzadelgaudio.services.AcquistoService;
import com.example.progettocozzadelgaudio.services.CarrelloService;
import com.example.progettocozzadelgaudio.support.DettaglioCarrelloDTO;
import com.example.progettocozzadelgaudio.support.RispostaAcquisto;
import com.example.progettocozzadelgaudio.support.exception.BudgetInsufficienteException;
import com.example.progettocozzadelgaudio.support.exception.PrezzoDifferenteException;
import com.example.progettocozzadelgaudio.support.exception.QuantitaInsufficienteException;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin("*")
@RequestMapping("/carrello")
public class CarrelloController {

    @Autowired
    private CarrelloService carrelloService;

    @Autowired
    private AcquistoService acquistoService;

    private static final int SOGLIA=5;

    @GetMapping
    @PreAuthorize("hasAuthority('farmacia')")
    public ResponseEntity visualizzaCarrello() {
        Carrello ret = carrelloService.visualizzaCarrello();
        return new ResponseEntity(ret,HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('farmacia')")
    public ResponseEntity aggiungiAlCarrello(@Valid @RequestBody Map<String,String> prodQta) {
        try{
            return new ResponseEntity(carrelloService.aggiungiAlCarrello(Long.parseLong(prodQta.get("idProdotto")),Integer.parseInt(prodQta.get("qta"))),HttpStatus.OK);
        }catch (QuantitaInsufficienteException e){
            return new ResponseEntity("ERROR_QUANTITY_NOT_ENOUGH", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    @PreAuthorize("hasAuthority('farmacia')")
    public ResponseEntity modificaCarrello(@Valid @RequestBody Map<String,String> prodQta) { //quantita in meno o in piu
        try{
            return new ResponseEntity(carrelloService.modificaCarrello(Long.parseLong(prodQta.get("idProdotto")),Integer.parseInt(prodQta.get("qta"))),HttpStatus.OK);
        }catch (QuantitaInsufficienteException e){
            return new ResponseEntity("ERROR_QUANTITY_NOT_ENOUGH", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('farmacia')")
    public ResponseEntity eliminaProdottoInCarrello(@Valid @RequestBody Prodotto prodotto) {
        try{
            int quantitaPerEliminazione=0;
            return new ResponseEntity(carrelloService.modificaCarrello(prodotto.getId(),quantitaPerEliminazione),HttpStatus.OK);
        }catch (QuantitaInsufficienteException e){ //non verrà mai generata
            return new ResponseEntity( HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/acquisto")
    @PreAuthorize("hasAuthority('farmacia')")
    public ResponseEntity acquistaCarrello(@RequestBody List<DettaglioCarrelloDTO> carrelloClient){
        int cont=0;
        while(cont<SOGLIA)
        try{
            return new ResponseEntity(acquistoService.acquista(carrelloClient), HttpStatus.OK);
        }catch(QuantitaInsufficienteException e ) {
            return new ResponseEntity(new RispostaAcquisto("ERROR_PURCHASE_FAILED",e.getIdProdotto()), HttpStatus.BAD_REQUEST);
        }catch(PrezzoDifferenteException e) {
            return new ResponseEntity(new RispostaAcquisto("ERROR_PRICE_CHANGED", e.getIdProdotto()), HttpStatus.BAD_REQUEST);
        }catch(BudgetInsufficienteException e){
            return new ResponseEntity("ERROR_BUDGET_NOT_ENOUGH",HttpStatus.BAD_REQUEST);
        }catch(OptimisticLockException e) {
            cont++;
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
