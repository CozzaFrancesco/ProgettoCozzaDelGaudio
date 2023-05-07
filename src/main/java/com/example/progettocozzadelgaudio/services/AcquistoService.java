package com.example.progettocozzadelgaudio.services;

import com.example.progettocozzadelgaudio.authentication.Utils;
import com.example.progettocozzadelgaudio.entities.*;
import com.example.progettocozzadelgaudio.repositories.*;
import com.example.progettocozzadelgaudio.support.exception.QuantitaInsufficienteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.StringTokenizer;


@Service
public class AcquistoService {

    @Autowired
    private FarmaciaRepository farmaciaRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private DettaglioCarrelloRepository dettaglioCarrelloRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private DettaglioMagazzinoRepository dettaglioMagazzinoRepository;

    @Autowired
    private MagazzinoRepository magazzinoRepository;

    @Transactional
    public Magazzino acquista() throws QuantitaInsufficienteException {

        String emailFarmacia = Utils.getEmail();
        StringTokenizer st=new StringTokenizer("@");
        String partitaIva=st.nextToken();
        Farmacia farmacia=farmaciaRepository.findByPartitaIva(partitaIva);
        Carrello carrello=farmacia.getCarrello();
        Collection<DettaglioCarrello> listaDC= carrello.getDettaglioCarrello();
        for(DettaglioCarrello dc: listaDC)
        {
            Prodotto prodotto = prodottoRepository.findById(dc.getProdotto().getId());
            if(prodotto.getQta_inStock()<dc.getQuantita())
                throw new QuantitaInsufficienteException();
        }

        for(DettaglioCarrello dc: listaDC)
        {
            Prodotto prodotto= prodottoRepository.findById(dc.getProdotto().getId());
            prodotto.setQta_inStock(prodotto.getQta_inStock()-dc.getQuantita());
            prodottoRepository.save(prodotto);
        }

        Magazzino magazzino= farmacia.getMagazzino();
        Collection<DettaglioMagazzino> listaDM = magazzino.getDettaglioMagazzino();

        for(DettaglioCarrello dc: listaDC) {
            boolean trovato=false;
            for (DettaglioMagazzino dm : listaDM)
                if(dm.getProdotto().getId()==dc.getProdotto().getId()) {
                    dm.setQuantita(dm.getQuantita() + dc.getQuantita());
                    dettaglioMagazzinoRepository.save(dm);
                    trovato=true;
                }
            if(!trovato){
                DettaglioMagazzino dettaglioMagazzino= new DettaglioMagazzino();
                dettaglioMagazzino.setQuantita(dc.getQuantita());
                dettaglioMagazzino.setProdotto(dc.getProdotto());
                magazzino.getDettaglioMagazzino().add(dettaglioMagazzino);
                dettaglioMagazzinoRepository.save(dettaglioMagazzino);
            }
        }
        Magazzino risultato = magazzinoRepository.save(magazzino);

        for(DettaglioCarrello dc: listaDC)
            dettaglioCarrelloRepository.delete(dc);

        listaDC.clear();
        carrelloRepository.save(carrello);
        return risultato;
    }
}
