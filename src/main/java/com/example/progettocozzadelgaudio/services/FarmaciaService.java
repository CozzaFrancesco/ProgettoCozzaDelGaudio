package com.example.progettocozzadelgaudio.services;

import com.example.progettocozzadelgaudio.authentication.Utils;
import com.example.progettocozzadelgaudio.entities.Farmacia;
import com.example.progettocozzadelgaudio.entities.Prodotto;
import com.example.progettocozzadelgaudio.repositories.FarmaciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.awt.print.Pageable;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

@Service
public class FarmaciaService {

    @Autowired
    private FarmaciaRepository farmaciaRepository;

    @Transactional
    public Farmacia aggiornaBudget(Long id, Double budgetAggiuntivo) {
        Farmacia farmacia=farmaciaRepository.findById(id);
        farmacia.setBudget(farmacia.getBudget()+budgetAggiuntivo);
        return farmaciaRepository.save(farmacia);
    }

    @Transactional
    public Farmacia visualizzaFarmacia() {
        String emailFarmacia = Utils.getEmail();
        StringTokenizer st=new StringTokenizer("@");
        String partitaIva=st.nextToken();
        Farmacia farmacia=farmaciaRepository.findByPartitaIva(partitaIva);
        return farmacia;
    }

    @Transactional
    public Farmacia modificaIndirizzoFarmacia(String nuovoIndirizzo) {
        Farmacia farmacia=visualizzaFarmacia();
        farmacia.setIndirizzo(nuovoIndirizzo);
        return farmaciaRepository.save(farmacia);
    }

    @Transactional
    public Farmacia modificaNomeFarmacia(String nuovoNome) {
        Farmacia farmacia=visualizzaFarmacia();
        farmacia.setNome(nuovoNome);
        return farmaciaRepository.save(farmacia);
    }

}
