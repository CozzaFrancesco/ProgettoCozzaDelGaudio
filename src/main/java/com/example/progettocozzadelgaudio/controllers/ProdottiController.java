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

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prodotti")
public class ProdottiController {

    @Autowired
    private ProdottoService prodottoService;

    @GetMapping
    @PreAuthorize("hasAuthority('farmacia') or hasAuthority('gestore')")
    public ResponseEntity visualizzaProdotti(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                             @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<Prodotto> risultato = prodottoService.mostraTuttiProdotti(pageNumber, pageSize, sortBy);
        return new ResponseEntity(risultato,HttpStatus.OK);
    }

    //non ne sono certo
    @GetMapping("/ricercaAvanzata")
    @PreAuthorize("hasAuthority('farmacia') or hasAuthority('gestore')")
    public ResponseEntity visualizzaProdottiAvanzata(@RequestParam(required = false) Map<String, String> variabiliPath){
        List<Prodotto> risultato= prodottoService.trovaProdottoConRicercaAvanzata(variabiliPath.get("nome"),
                variabiliPath.get("principioAttivo"),variabiliPath.get("formaFarmaceutica"));
                //i campi possono anche non essere tutti presenti, o tutti null
        return new ResponseEntity(risultato,HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('gestore')")
    public ResponseEntity aggiungiProdotto(@RequestBody @Valid Prodotto prodotto) {
        try {
            Prodotto risultato = prodottoService.aggiungiProdotto(prodotto.getNome(),prodotto.getPrincipioAttivo(),
                    prodotto.getPrezzoUnitario(),prodotto.getFormaFarmaceutica(),prodotto.getQtaInStock());
            return new ResponseEntity(risultato,HttpStatus.OK);
        }catch(ProdottoGiaEsistenteException e) {
            return new ResponseEntity("ERROR_PRODUCT_ALREADY_EXISTS", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('farmacia') or hasAuthority('gestore')")
    public ResponseEntity visualizzaProdotto(@Valid @PathVariable("id") Long id) {
        Prodotto ret = prodottoService.trovaProdottoDaId(id);
        return new ResponseEntity(ret,HttpStatus.OK);
    }

    @PutMapping("/{id}/aggiorna")
    @PreAuthorize("hasAuthority('gestore')")
    public ResponseEntity modifica(@Valid @PathVariable("id") Long id,
                                   @Valid @RequestBody Map<String,String> modificaMap){
        try {
            return new ResponseEntity(prodottoService.aggiornaProdotto(id, Integer.parseInt(modificaMap.get("qta")),
                    Double.parseDouble(modificaMap.get("prezzo"))),HttpStatus.OK);
        }catch(AggiornamentoFallitoException e){
            return new ResponseEntity("ERROR_UPDATING_FAILED",HttpStatus.BAD_REQUEST);
        }
    }
}
