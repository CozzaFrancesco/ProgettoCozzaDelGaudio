package com.example.progettocozzadelgaudio.controllers;

import com.example.progettocozzadelgaudio.services.EliminazioneService;
import com.example.progettocozzadelgaudio.services.RegistrazioneService;
import com.example.progettocozzadelgaudio.support.exception.GestoreGiaEsistenteException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/gestore")
public class GestoreController {

    @Autowired
    private RegistrazioneService registrazioneService;

    @Autowired
    private EliminazioneService eliminazioneService;

    @PostMapping
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity aggiungiGestore(@RequestBody @Valid Map<String,String> aggiungiMap) {
        try {
            registrazioneService.registraGestore(aggiungiMap.get("nome"),aggiungiMap.get("email"),aggiungiMap.get("password"));
            return new ResponseEntity(HttpStatus.OK);
        }catch(GestoreGiaEsistenteException e) {
            return new ResponseEntity("ERROR_MANAGER_ALREADY_EXISTS", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity resettaGestore(@RequestBody @Valid Map<String,String> resetMap) {
        try {

            eliminazioneService.eliminaGestore(resetMap.get("usernameVecchioGestore"));
            registrazioneService.registraGestore(resetMap.get("nome"),resetMap.get("email"),resetMap.get("password"));
            return new ResponseEntity(HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity("ERROR_MANAGER_NOT_EXISTS", HttpStatus.BAD_REQUEST);
        }
    }


}
