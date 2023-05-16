package com.example.progettocozzadelgaudio.controllers;

import com.example.progettocozzadelgaudio.entities.Prodotto;
import com.example.progettocozzadelgaudio.services.ProdottoService;
import com.example.progettocozzadelgaudio.support.exception.AggiornamentoFallitoException;
import com.example.progettocozzadelgaudio.support.exception.ProdottoGiaEsistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/prodotti")
public class ProdottiController {

    @Autowired
    private ProdottoService prodottoService;

    @GetMapping
    @PreAuthorize("hasAuthority('farmacia') or hasAuthority('gestore')")
    public Collection<Prodotto> visualizzaProdotti(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<Prodotto> risultato = prodottoService.mostraTuttiProdotti(pageNumber, pageSize, sortBy);
        return risultato;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('gestore')")
    public ResponseEntity aggiungiProdotto(@RequestBody @Valid String nome, @RequestBody @Valid String principioAttivo,
                                           @RequestBody @Valid double prezzoUnitario, @RequestBody @Valid String formaFarmaceutica,
                                           @RequestBody @Valid Integer qtaInStock) {
        try {
            Prodotto prodotto = prodottoService.aggiungiProdotto(nome, principioAttivo, prezzoUnitario, formaFarmaceutica, qtaInStock);
            return new ResponseEntity(prodotto,HttpStatus.OK);
        }catch(ProdottoGiaEsistenteException e) {
            return new ResponseEntity("ERROR_PRODUCT_ALREADY_EXISTS", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('farmacia') or hasAuthority('gestore')")
    public Prodotto visualizzaProdotto(@Valid @PathVariable("id") Long id) {
        return prodottoService.trovaProdottoDaId(id);
    }

    @PutMapping("/{id}/aggiorna")
    @PreAuthorize("hasAuthority('gestore')")
    public ResponseEntity modifica(@Valid @PathVariable("id") Long id, @Valid @RequestBody int quantita, @Valid @RequestBody double prezzo) {
        try {
            return new ResponseEntity(prodottoService.aggiornaProdotto(id, quantita, prezzo),HttpStatus.OK);
        }catch(AggiornamentoFallitoException e){
            return new ResponseEntity("ERROR_UPDATING_FAILED",HttpStatus.BAD_REQUEST);
        }
    }


}
