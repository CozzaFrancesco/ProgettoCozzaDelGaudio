package com.example.progettocozzadelgaudio.services;

import com.example.progettocozzadelgaudio.authentication.KeyCloak;
import com.example.progettocozzadelgaudio.entities.DettaglioCarrello;
import com.example.progettocozzadelgaudio.entities.DettaglioMagazzino;
import com.example.progettocozzadelgaudio.entities.Farmacia;
import com.example.progettocozzadelgaudio.entities.Carrello;
import java.util.Collection;
import com.example.progettocozzadelgaudio.entities.Magazzino;
import com.example.progettocozzadelgaudio.repositories.*;
import com.example.progettocozzadelgaudio.support.exception.GestoreGiaEsistenteException;
import com.example.progettocozzadelgaudio.support.exception.GestoreInesistenteException;
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

    //solo admin
    @Transactional(readOnly = false, rollbackFor = GestoreGiaEsistenteException.class)
    public void eliminaGestore(String username) throws GestoreInesistenteException {
        if(!kc.eliminaGestore(username))
            throw new GestoreInesistenteException();
    }
}
