package com.example.progettocozzadelgaudio.services;

import com.example.progettocozzadelgaudio.entities.Appuntamento;
import com.example.progettocozzadelgaudio.entities.Farmacia;
import com.example.progettocozzadelgaudio.entities.Magazzino;
import com.example.progettocozzadelgaudio.entities.Visita;
import com.example.progettocozzadelgaudio.repositories.AppuntamentoRepository;
import com.example.progettocozzadelgaudio.repositories.FarmaciaRepository;
import com.example.progettocozzadelgaudio.repositories.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private FarmaciaRepository farmaciaRepository;

    @Autowired
    private VisitaRepository visitaRepository;

    @Autowired
    private AppuntamentoRepository appuntamentoRepository;

    @Transactional
    public Collection<Farmacia> visualizzaFarmaciePerCitta(String citta, int numeroPagina, int dimensionePagina) {
        Pageable paging = (Pageable) PageRequest.of(numeroPagina, dimensionePagina);
        List<Farmacia> pagedResult = farmaciaRepository.findAllByCitta(citta, paging);
        if ( !pagedResult.isEmpty() ) {
            return pagedResult;
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional
    public Magazzino visualizzaMagazzinoPerFarmacia(Long idFarmacia) {
        Farmacia farmacia=farmaciaRepository.findById(idFarmacia);
        return farmacia.getMagazzino();
    }

    @Transactional
    public Collection<Visita> visualizzaVisitePerFarmacia(Long idFarmacia) {
        Farmacia farmacia=farmaciaRepository.findById(idFarmacia);
        return farmacia.getVisite();
    }

    @Transactional
    public Collection<LocalTime> visulizzaOrariDisponibiliPerVisitainFarmaciaEData(Long idFarmacia, Long idVisita,
                                                                                   int anno, int mese, int giorno){
        Collection<LocalTime> ret=new ArrayList<>();
        Farmacia farmacia= farmaciaRepository.findById(idFarmacia);
        Visita visita= visitaRepository.findById(idVisita);
        LocalDate data = LocalDate.of(anno,mese,giorno);
        int durataVisita= visita.getDurata();

        Collection<Appuntamento> visiteInData = appuntamentoRepository.findByFarmaciaAndData(farmacia,data);

        LocalTime cursore=farmacia.getOrarioInizioVisite();

        while(cursore.isBefore(farmacia.getOrarioFineVisite().minusMinutes(durataVisita))) {
            Collection<Appuntamento> visiteIR=visiteInRange(visiteInData,cursore,cursore.plusMinutes(durataVisita));
            if(visiteIR.size() < farmacia.getNumDipendenti()-1)
                ret.add(cursore);
            cursore=cursore.plusMinutes(durataVisita);
        }
        return ret;
    }

    private Collection<Appuntamento> visiteInRange(Collection<Appuntamento> visiteInData, LocalTime inizio, LocalTime fine) {
        Collection<Appuntamento> ret=new ArrayList<>();
        for(Appuntamento app:visiteInData) {
            LocalTime orarioFineAppCorrente=app.getOrario().plusMinutes(app.getVisita().getDurata());
            if (app.getOrario().equals(inizio)
                    || (app.getOrario().isAfter(inizio)) && orarioFineAppCorrente.isBefore(fine)
                    || (app.getOrario().isBefore(inizio)) && ( orarioFineAppCorrente.isAfter(fine) || orarioFineAppCorrente.equals(fine)) )
                ret.add(app);
        }
        return ret;
    }
}
