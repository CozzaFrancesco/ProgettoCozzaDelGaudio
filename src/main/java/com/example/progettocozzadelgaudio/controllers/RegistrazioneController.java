package com.example.progettocozzadelgaudio.controllers;

import com.example.progettocozzadelgaudio.entities.Cliente;
import com.example.progettocozzadelgaudio.entities.Farmacia;
import com.example.progettocozzadelgaudio.services.RegistrazioneService;
import com.example.progettocozzadelgaudio.support.exception.CFClienteGiaEsistenteException;
import com.example.progettocozzadelgaudio.support.exception.GestoreGiaEsistenteException;
import com.example.progettocozzadelgaudio.support.exception.PivaFarmaciaGiaEsistenteException;
import org.apache.james.mime4j.field.datetime.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;


@RestController
@CrossOrigin("*")
@RequestMapping("/registrazione")
public class RegistrazioneController {

    @Autowired
    RegistrazioneService registrazioneService;

    @PostMapping("/farmacia")
    public ResponseEntity registraFarmacia(@RequestBody @Valid Map<String,String> loginMap ){
        try{
            Farmacia farmacia=registrazioneService.registraFarmacia(loginMap.get("nome"),loginMap.get("indirizzo"),
                    Double.parseDouble(loginMap.get("budget")),loginMap.get("citta"),
                    loginMap.get("partitaIva"),Integer.parseInt(loginMap.get("numeroDipendenti")),
                    LocalTime.parse(loginMap.get("orarioInizioVisita")),LocalTime.parse(loginMap.get("orarioFineVisita")),
                    loginMap.get("password"));
            return new ResponseEntity<>(farmacia,HttpStatus.OK);
        }catch(PivaFarmaciaGiaEsistenteException e){
            return new ResponseEntity<>("ERROR_MAIL_PHARMACY_ALREADY_EXISTS", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/cliente")
    public ResponseEntity registraCliente(@RequestBody @Valid  Map<String,String> loginMap){
        try{
            LocalDate ld=null;
            if(loginMap.get("dataNascita")!=null) {
                String[] data= loginMap.get("dataNascita").split("T");
                ld = LocalDate.parse(data[0]);
            }
            Cliente cliente=registrazioneService.registraCliente(loginMap.get("nome"),loginMap.get("cognome"),loginMap.get("codiceFiscale"),ld,loginMap.get("citta"),loginMap.get("indirizzo"),loginMap.get("password"));
            return new ResponseEntity<>(cliente,HttpStatus.OK);
        }catch(CFClienteGiaEsistenteException e){
            return new ResponseEntity<>("ERROR_MAIL_USER_ALREADY_EXISTS", HttpStatus.BAD_REQUEST);
        }
    }
}
