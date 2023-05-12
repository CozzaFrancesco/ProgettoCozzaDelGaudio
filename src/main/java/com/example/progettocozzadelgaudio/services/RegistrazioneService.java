package com.example.progettocozzadelgaudio.services;

import com.example.progettocozzadelgaudio.authentication.KeyCloak;
import com.example.progettocozzadelgaudio.entities.Farmacia;
import com.example.progettocozzadelgaudio.entities.Magazzino;
import com.example.progettocozzadelgaudio.repositories.CarrelloRepository;
import com.example.progettocozzadelgaudio.repositories.ClienteRepository;
import com.example.progettocozzadelgaudio.repositories.FarmaciaRepository;
import com.example.progettocozzadelgaudio.repositories.MagazzinoRepository;
import com.example.progettocozzadelgaudio.support.exception.CFClienteGiaEsistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.time.LocalDate;

import java.util.List;
import com.example.progettocozzadelgaudio.support.exception.PivaFarmaciaGiaEsistenteException;
import com.example.progettocozzadelgaudio.entities.Carrello;
import com.example.progettocozzadelgaudio.support.exception.GestoreGiaEsistenteException;
import com.example.progettocozzadelgaudio.entities.Cliente;

@Service
public class RegistrazioneService {

    @Autowired
    private FarmaciaRepository farmaciaRepository;

    @Autowired
    private MagazzinoRepository magazzinoRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private ClienteRepository clienteRepository;

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

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Cliente registraCliente(String nome, String cognome, String codiceFiscale, int giornoNascita, int meseNascita, int annoNascita, String citta, String indirizzo, String password ) throws CFClienteGiaEsistenteException {
        if ( clienteRepository.existsByCodiceFiscale(codiceFiscale) || ! kc.registraCliente(nome,cognome,codiceFiscale,password) ) {
            throw new CFClienteGiaEsistenteException();
        }

        Cliente cliente=new Cliente();
        cliente.setNome(nome);
        cliente.setCognome(cognome);
        cliente.setCodiceFiscale(codiceFiscale);
        cliente.setCitta(citta);
        cliente.setIndirizzo(indirizzo);
        LocalDate date= LocalDate.of(annoNascita,meseNascita,giornoNascita);

        return clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<Farmacia> getAllUsers() {
        return farmaciaRepository.findAll();
    }
}
