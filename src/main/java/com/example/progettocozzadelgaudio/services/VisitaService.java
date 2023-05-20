package com.example.progettocozzadelgaudio.services;

import com.example.progettocozzadelgaudio.entities.Visita;
import com.example.progettocozzadelgaudio.repositories.VisitaRepository;
import com.example.progettocozzadelgaudio.support.exception.VisitaGiaEsistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class VisitaService {

    @Autowired
    VisitaRepository visitaRepository;
    @Transactional
    public Collection<Visita> visualizzaTutte(){
        return visitaRepository.findAll();
    }

    @Transactional
    public Visita aggiungiVisita(Visita visita) throws VisitaGiaEsistenteException{
        if(visitaRepository.existsByNome(visita.getNome()))
            throw new VisitaGiaEsistenteException();
        return visitaRepository.save(visita);
    }
}
