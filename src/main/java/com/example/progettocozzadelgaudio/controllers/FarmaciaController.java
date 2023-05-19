package com.example.progettocozzadelgaudio.controllers;

import com.example.progettocozzadelgaudio.entities.Farmacia;
import com.example.progettocozzadelgaudio.services.FarmaciaService;
import com.example.progettocozzadelgaudio.services.MagazzinoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/farmacia")
public class FarmaciaController {

    @Autowired
    private FarmaciaService farmaciaService;

    @Autowired
    private MagazzinoService magazzinoService;

    @GetMapping
    @PreAuthorize("hasAuthority('farmacia')")
    public ResponseEntity visualizzaAreaPersonale() {
        return new ResponseEntity(farmaciaService.visualizzaFarmacia(), HttpStatus.OK);
    }

    @PutMapping("/modificaNome")
    @PreAuthorize("hasAuthority('farmacia')")
    public ResponseEntity modificaNome(@RequestBody @Valid Map<String,String> nome) {
        Farmacia farmacia=farmaciaService.modificaNomeFarmacia(nome.get("nuovoNome"));
        return new ResponseEntity(farmacia,HttpStatus.OK);
    }

    @PutMapping("/modificaIndirizzo")
    @PreAuthorize("hasAuthority('farmacia')")
    public ResponseEntity modificaIndirizzo(@RequestBody @Valid Map<String,String> indirizzo) {
        Farmacia farmacia=farmaciaService.modificaIndirizzoFarmacia(indirizzo.get("nuovoIndirizzo"));
        return new ResponseEntity(farmacia,HttpStatus.OK);
    }

    @GetMapping("/appuntamentiPersonali/{data}")
    @PreAuthorize("hasAuthority('farmacia')")
    public ResponseEntity visualizzaAppuntamentiPerData(@PathVariable("data") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate data) {
        return new ResponseEntity(farmaciaService.visualizzaAppuntamenti(data.getYear(),data.getMonthValue(),data.getDayOfMonth()),HttpStatus.OK);
    }

    @GetMapping("/magazzino")
    @PreAuthorize("hasAuthority('farmacia')")
    public ResponseEntity visualizzaMagazzino() {
        return new ResponseEntity(magazzinoService.visualizzaMagazzino(),HttpStatus.OK);
    }

    @GetMapping("/magazzino/{nome}")
    @PreAuthorize("hasAuthority('farmacia')")
    public ResponseEntity visualizzaMagazzinoPerNome(@PathVariable @Valid String nome) {
        return new ResponseEntity(magazzinoService.visualizzaMagazzinoPerNome(nome),HttpStatus.OK);
    }

    @GetMapping("/magazzino/{principioAttivo}")
    @PreAuthorize("hasAuthority('farmacia')")
    public ResponseEntity visualizzaMagazzinoPerPrincipioAttivo(@PathVariable @Valid String principioAttivo) {
        return new ResponseEntity(magazzinoService.visualizzaMagazzinoPerPrincipioAttivo(principioAttivo),HttpStatus.OK);
    }
}
