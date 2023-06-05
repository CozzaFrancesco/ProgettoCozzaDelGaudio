package com.example.progettocozzadelgaudio.controllers;

import com.example.progettocozzadelgaudio.entities.Visita;
import com.example.progettocozzadelgaudio.services.VisitaService;
import com.example.progettocozzadelgaudio.support.exception.VisitaGiaEsistenteException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/visite")
public class VisiteController {

    @Autowired
    VisitaService visitaService;
    @GetMapping
    @PreAuthorize("hasAuthority('gestore')")
    public ResponseEntity vediTutte(){
        return new ResponseEntity(visitaService.visualizzaTutte(), HttpStatus.OK);
    }

    @PostMapping("/aggiungi")
    @PreAuthorize("hasAuthority('gestore')")
    public ResponseEntity aggiungiNuovaVisita(@RequestBody @Valid Visita visita){
        try{
            return new ResponseEntity(visitaService.aggiungiVisita(visita),HttpStatus.OK);
        }catch(VisitaGiaEsistenteException e){
            return new ResponseEntity("ERROR_EXAMINATION_ALREADY_EXISTS",HttpStatus.BAD_REQUEST);
        }
    }
}
