package com.example.progettocozzadelgaudio.services;

import com.example.progettocozzadelgaudio.authentication.KeyCloak;
import com.example.progettocozzadelgaudio.entities.Farmacia;
import com.example.progettocozzadelgaudio.entities.Magazzino;
import com.example.progettocozzadelgaudio.repositories.CarrelloRepository;
import com.example.progettocozzadelgaudio.repositories.FarmaciaRepository;
import com.example.progettocozzadelgaudio.repositories.MagazzinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import com.example.progettocozzadelgaudio.support.exception.PivaFarmaciaGiaEsistenteException;
import com.example.progettocozzadelgaudio.entities.Carrello;
import com.example.progettocozzadelgaudio.support.exception.GestoreGiaEsistenteException;

@Service
public class RegistrazioneService {

    @Autowired
    private FarmaciaRepository farmaciaRepository;

    @Autowired
    private MagazzinoRepository magazzinoRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    private KeyCloak kc=new KeyCloak();


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Farmacia registraFarmacia(String nome, String indirizzo, double budget, String citta, String partitaIva, String password ) throws PivaFarmaciaGiaEsistenteException {
        if ( farmaciaRepository.existsByPartitaIva(partitaIva) || ! kc.registraFarmacia(nome,partitaIva,password) ) {
            throw new PivaFarmaciaGiaEsistenteException();
        }
        Farmacia farmacia=new Farmacia();
        farmacia.setNome(nome);
        farmacia.setIndirizzo(indirizzo);
        farmacia.setBudget(budget);
        farmacia.setCitta(citta);
        farmacia.setPartitaIva(partitaIva);

        Carrello carrello=new Carrello();
        farmacia.setCarrello(carrello);
        carrello.setFarmacia(farmacia);

        Magazzino magazzino=new Magazzino();
        farmacia.setMagazzino(magazzino);
        magazzino.setFarmacia(farmacia);


        Farmacia risultato=farmaciaRepository.save(farmacia);
        magazzinoRepository.save(magazzino);
        carrelloRepository.save(carrello);
        return risultato;
    }

    public void registraGestore(String nome, String email, String password) throws GestoreGiaEsistenteException{
        if(! kc.registraGestore(nome,email,password)) {
            throw new GestoreGiaEsistenteException();
        }
    }
    @Transactional(readOnly = true)
    public List<Farmacia> getAllUsers() {
        return farmaciaRepository.findAll();
    }
}
