package com.example.progettocozzadelgaudio.controllers;

import com.example.progettocozzadelgaudio.services.AppuntamentoService;
import com.example.progettocozzadelgaudio.services.ClienteService;
import com.example.progettocozzadelgaudio.services.FarmaciaService;
import com.example.progettocozzadelgaudio.support.exception.AggiornamentoFallitoException;
import com.example.progettocozzadelgaudio.support.exception.AppuntamentoNonPiuDisponibileException;
import com.example.progettocozzadelgaudio.support.exception.DataNonValidaException;
import jakarta.persistence.PessimisticLockException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@RestController
@RequestMapping("/farmacie")
public class GestioneFarmaciaController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private FarmaciaService farmaciaService;

    @Autowired
    private AppuntamentoService appuntamentoService;

    private static final int SOGLIA=5;


    //home page cliente e gestore
    @GetMapping
    @PreAuthorize("hasAuthority('cliente') or hasAuthority('gestore')")
    public ResponseEntity visualizzaFarmacie(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        return new ResponseEntity(clienteService.visualizzaFarmacie(pageNumber, pageSize, sortBy),HttpStatus.OK);
    }

    @GetMapping("/{citta}")
    @PreAuthorize("hasAuthority('cliente') or hasAuthority('gestore')")
    public ResponseEntity visualizzaFarmaciePerCitta(@PathVariable @Valid String citta) {
        return new ResponseEntity(clienteService.visualizzaFarmaciePerCitta(citta), HttpStatus.OK);
    }


    @GetMapping("/{idFarmacia}/magazzino")
    @PreAuthorize("hasAuthority('cliente')")
    public ResponseEntity visualizzaMagazzinoFarmacia(@PathVariable @Valid Long idFarmacia) {
        return new ResponseEntity(clienteService.visualizzaMagazzinoPerFarmacia(idFarmacia),HttpStatus.OK);
    }

    @GetMapping("/{idFarmacia}/magazzino/{nome}")
    @PreAuthorize("hasAuthority('cliente')") //qui si ritorna una lista di dettaglio magazzino
    public ResponseEntity visualizzaMagazzinoFarmaciaPerNome(@PathVariable @Valid Long idFarmacia,@PathVariable @Valid String nome) {
        return new ResponseEntity(clienteService.visualizzaMagazzinoPerFarmaciaENomeProdotto(idFarmacia,nome),HttpStatus.OK);
    }

    @GetMapping("/{idFarmacia}/visite")
    @PreAuthorize("hasAuthority('cliente')")
    public ResponseEntity visualizzaVisiteFarmacia(@PathVariable @Valid Long idFarmacia) {
        return new ResponseEntity(clienteService.visualizzaVisitePerFarmacia(idFarmacia),HttpStatus.OK);
    }

    @GetMapping("/{idFarmacia}/visite/{idVisita}/{data}/orariDisponibili")
    @PreAuthorize("hasAuthority('cliente')")
    public ResponseEntity orariDisponibiliPerVisitaFarmacia(@PathVariable @Valid Long idFarmacia,
                                                            @PathVariable @Valid Long idVisita,
                                                            @PathVariable("data") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate data) {
        try {
            return new ResponseEntity(clienteService.visualizzaOrariDisponibiliPerVisitainFarmaciaEData(idFarmacia, idVisita, data.getYear(), data.getMonthValue(), data.getDayOfMonth()), HttpStatus.OK);
        }catch(DataNonValidaException e) {
            return new ResponseEntity("ERROR_INVALID_DATE",HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{idFarmacia}/visite/{idVisita}/{data}/{orario}/prenota")
    @PreAuthorize("hasAuthority('cliente')")
    public ResponseEntity prenotaVisitaFarmacia(@PathVariable @Valid Long idFarmacia,
                                                @PathVariable @Valid Long idVisita,
                                                @PathVariable("data") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate data,
                                                @PathVariable("orario") @DateTimeFormat(pattern="hh:mm") LocalTime orario) {
        int cont=0;
        while(cont<SOGLIA)
        try{
            return new ResponseEntity(appuntamentoService.creaAppuntamento(idFarmacia,idVisita,data.getYear(),
                    data.getMonthValue(),data.getDayOfMonth(),orario.getHour(),orario.getMinute()),HttpStatus.OK);
        }catch(AppuntamentoNonPiuDisponibileException e){
            return new ResponseEntity("ERROR_BOOKING_UNAVAILABLE_",HttpStatus.BAD_REQUEST);
        }catch(PessimisticLockException e) {
            cont++;
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{idFarmacia}/aggiungiDipendenti")
    @PreAuthorize("hasAuthority('gestore')")
    public ResponseEntity aggiungiDipendentiFarmacia(@PathVariable @Valid Long idFarmacia, @RequestBody @Valid Map<String,String> quantita) {
        try {
            return new ResponseEntity(farmaciaService.aggiungiDipendenti(idFarmacia, Integer.parseInt(quantita.get("quantita"))),HttpStatus.OK);
        }catch(AggiornamentoFallitoException e) {
            return new ResponseEntity("ERROR_UPDATING_FAILED",HttpStatus.BAD_REQUEST);
        }
    }

    /*
    forse va tolto?
    se rimuovo dipendenti ma mi hanno prenotato delle visite che sfrutta quel dipendnete?
     */
    @PutMapping("/{idFarmacia}/rimuoviDipendenti")
    @PreAuthorize("hasAuthority('gestore')")
    public ResponseEntity rimuoviDipendentiFarmacia(@PathVariable @Valid Long idFarmacia, @RequestBody @Valid Map<String,String> quantita) {
        try {
            return new ResponseEntity(farmaciaService.rimuoviDipendenti(idFarmacia,Integer.parseInt(quantita.get("quantita"))),HttpStatus.OK);
        }catch(AggiornamentoFallitoException e) {
            return new ResponseEntity("ERROR_UPDATING_FAILED",HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{idFarmacia}/aggiornaBudget")
    @PreAuthorize("hasAuthority('gestore')")
    public ResponseEntity aggiornaBudgetFarmacia(@PathVariable @Valid Long idFarmacia, @RequestBody @Valid Map<String,String> quantita) {
        try {
            return new ResponseEntity(farmaciaService.aggiornaBudget(idFarmacia,Double.parseDouble(quantita.get("quantita"))),HttpStatus.OK);
        }catch(AggiornamentoFallitoException e) {
            return new ResponseEntity("ERROR_UPDATING_FAILED",HttpStatus.BAD_REQUEST);
        }
    }
}
