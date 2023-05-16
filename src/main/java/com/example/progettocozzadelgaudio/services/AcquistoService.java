package com.example.progettocozzadelgaudio.services;

import com.example.progettocozzadelgaudio.authentication.Utils;
import com.example.progettocozzadelgaudio.entities.*;
import com.example.progettocozzadelgaudio.repositories.*;
import com.example.progettocozzadelgaudio.support.exception.BudgetInsufficienteException;
import com.example.progettocozzadelgaudio.support.exception.QuantitaInsufficienteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.util.*;


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
    public Magazzino acquista() throws QuantitaInsufficienteException,BudgetInsufficienteException {
        double totaleCarrello=0.0;
        String emailFarmacia = Utils.getEmail();
        StringTokenizer st=new StringTokenizer(emailFarmacia,"@");
        String partitaIva=st.nextToken();
        Farmacia farmacia=farmaciaRepository.findByPartitaIva(partitaIva);
        Carrello carrello=farmacia.getCarrello();
        Collection<DettaglioCarrello> listaDC= carrello.getDettaglioCarrello();

        Map<Prodotto,Integer> prodottiNelCarrello=new HashMap<>();
        for(DettaglioCarrello dc: listaDC)
        {
            Prodotto prodotto = prodottoRepository.findByIdWithLock(dc.getProdotto().getId());
            if(prodotto.getQta_inStock()<dc.getQuantita())
                throw new QuantitaInsufficienteException();
            prodottiNelCarrello.put(prodotto,dc.getQuantita());
            totaleCarrello=totaleCarrello+dc.getPrezzo();
        }

        if(totaleCarrello > farmacia.getBudget())
            throw new BudgetInsufficienteException();

        for(Prodotto p: prodottiNelCarrello.keySet())
        {
            p.setQta_inStock(p.getQta_inStock()-prodottiNelCarrello.get(p));
            prodottoRepository.save(p);
        }

        Magazzino magazzino= farmacia.getMagazzino();
        Collection<DettaglioMagazzino> listaDM = magazzino.getDettaglioMagazzino();

        for(Prodotto p: prodottiNelCarrello.keySet()) {
            boolean trovato=false;
            for (DettaglioMagazzino dm : listaDM)
                if(dm.getProdotto().getId()==p.getId()) {
                    dm.setQuantita(dm.getQuantita() + prodottiNelCarrello.get(p));
                    dettaglioMagazzinoRepository.save(dm);
                    trovato=true;
                }
            if(!trovato){
                DettaglioMagazzino dettaglioMagazzino= new DettaglioMagazzino();
                dettaglioMagazzino.setQuantita(prodottiNelCarrello.get(p));
                dettaglioMagazzino.setProdotto(p);
                magazzino.getDettaglioMagazzino().add(dettaglioMagazzino);
                dettaglioMagazzinoRepository.save(dettaglioMagazzino);
            }
        }

        Magazzino risultato = magazzinoRepository.save(magazzino);

        for(DettaglioCarrello dc: listaDC)
            dettaglioCarrelloRepository.delete(dc);

        listaDC.clear();
        farmacia.setBudget(farmacia.getBudget()-totaleCarrello);
        farmaciaRepository.save(farmacia);
        carrelloRepository.save(carrello);
        return risultato;
    }
}
