package com.example.progettocozzadelgaudio.controllers;

import com.example.progettocozzadelgaudio.entities.Appuntamento;
import com.example.progettocozzadelgaudio.services.AppuntamentoService;
import com.example.progettocozzadelgaudio.services.ClienteService;
import com.example.progettocozzadelgaudio.support.exception.AppuntamentoNonDisdicibileException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private AppuntamentoService appuntamentoService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @PreAuthorize("hasAuthority('cliente')")
    public ResponseEntity visualizzaAreaPersonale(){
        return new ResponseEntity(clienteService.visualizzaCliente(),HttpStatus.OK);
    }

    @PutMapping("/modificaCitta")
    @PreAuthorize("hasAuthority('cliente')")
    public ResponseEntity modificaCitta(Map<String,String > citta){
        return new ResponseEntity<>(clienteService.modificaCitta(citta.get("citta")),HttpStatus.OK);
    }

    @PutMapping("/modificaIndirizzo")
    @PreAuthorize("hasAuthority('cliente')")
    public ResponseEntity modificaIndirizo(Map<String,String > indirizzo){
        return new ResponseEntity<>(clienteService.modificaIndirizzo(indirizzo.get("indirizzo")),HttpStatus.OK);
    }

    @GetMapping("/prenotazioni")
    @PreAuthorize("hasAuthority('cliente')")
    public ResponseEntity visualizzaAppuntamenti() {
        Collection<Appuntamento> ret=clienteService.visualizzaAppuntamenti();
        return new ResponseEntity(ret, HttpStatus.OK);
    }

    @DeleteMapping("/prenotazioni")
    @PreAuthorize("hasAuthority('cliente')")
    public ResponseEntity disdiciAppuntamento(@RequestBody @Valid Appuntamento appuntamento) {
        try {
            appuntamentoService.discidiAppuntamento(appuntamento.getId());
            return new ResponseEntity(HttpStatus.OK);
        }catch(AppuntamentoNonDisdicibileException e) {
            return new ResponseEntity("ERROR_BOOKING_NOT_ANNULABLE",HttpStatus.BAD_REQUEST);
        }
    }
}
