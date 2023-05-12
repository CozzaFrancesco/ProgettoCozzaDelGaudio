package com.example.progettocozzadelgaudio.services;

import com.example.progettocozzadelgaudio.authentication.KeyCloak;
import com.example.progettocozzadelgaudio.entities.DettaglioCarrello;
import com.example.progettocozzadelgaudio.entities.DettaglioMagazzino;
import com.example.progettocozzadelgaudio.entities.Farmacia;
import com.example.progettocozzadelgaudio.entities.Carrello;
import java.util.Collection;
import com.example.progettocozzadelgaudio.entities.Magazzino;
import com.example.progettocozzadelgaudio.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EliminazioneService {

    @Autowired
    private FarmaciaRepository farmaciaRepository;

    @Autowired
    private MagazzinoRepository magazzinoRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private DettaglioCarrelloRepository dettaglioCarrelloRepository;

    @Autowired
    private DettaglioMagazzinoRepository dettaglioMagazzinoRepository;

    private KeyCloak kc=new KeyCloak();

    //solo gestore
    @Transactional(readOnly = true)
    public boolean eliminaFarmacia(Long id) {
        Farmacia farmacia=farmaciaRepository.findById(id);

        if(! kc.eliminaFarmacia(farmacia.getPartitaIva()))
            return false;

        Carrello carrello=farmacia.getCarrello();
        Collection<DettaglioCarrello> listaDC=carrello.getDettaglioCarrello();
        for(DettaglioCarrello dc:listaDC)
            dettaglioCarrelloRepository.delete(dc);
        carrelloRepository.delete(carrello);

        Magazzino magazzino=farmacia.getMagazzino();
        Collection<DettaglioMagazzino> listaDM=magazzino.getDettaglioMagazzino();
        for(DettaglioMagazzino dm:listaDM)
            dettaglioMagazzinoRepository.delete(dm);
        magazzinoRepository.delete(magazzino);

        farmaciaRepository.delete(farmacia);
        return true;
    }

    //solo admin
    @Transactional
    public boolean eliminaGestore(String email) {
        return kc.eliminaGestore(email);
    }
}
