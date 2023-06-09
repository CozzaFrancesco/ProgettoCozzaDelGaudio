package com.example.progettocozzadelgaudio.services;

import com.example.progettocozzadelgaudio.entities.Carrello;
import com.example.progettocozzadelgaudio.entities.DettaglioCarrello;
import com.example.progettocozzadelgaudio.repositories.CarrelloRepository;
import com.example.progettocozzadelgaudio.repositories.DettaglioCarrelloRepository;
import com.example.progettocozzadelgaudio.repositories.FarmaciaRepository;
import com.example.progettocozzadelgaudio.repositories.ProdottoRepository;
import com.example.progettocozzadelgaudio.support.exception.ProdottoGiaEsistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.progettocozzadelgaudio.authentication.Utils;
import com.example.progettocozzadelgaudio.entities.Farmacia;
import com.example.progettocozzadelgaudio.entities.Prodotto;
import com.example.progettocozzadelgaudio.repositories.CarrelloRepository;
import java.util.Iterator;
import com.example.progettocozzadelgaudio.support.exception.QuantitaInsufficienteException;

import java.util.Collection;
import java.util.StringTokenizer;

@Service
public class CarrelloService {

    @Autowired
    private FarmaciaRepository farmaciaRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private DettaglioCarrelloRepository dettaglioCarrelloRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Transactional(readOnly = true)
    public Carrello visualizzaCarrello() {
        String emailFarmacia = Utils.getEmail();
        StringTokenizer st=new StringTokenizer(emailFarmacia,"@");
        String partitaIva=st.nextToken();
        Farmacia farmacia=farmaciaRepository.findByPartitaIva(partitaIva);
        return farmacia.getCarrello();
    }

    @Transactional(rollbackFor = QuantitaInsufficienteException.class)
    public Carrello aggiungiAlCarrello(Long idProdotto, Integer quantita) throws QuantitaInsufficienteException{
        Prodotto prodotto=prodottoRepository.findById(idProdotto);

        if(prodotto.getQtaInStock()<quantita)
            throw new QuantitaInsufficienteException(prodotto.getId());

        boolean giaInCarrello=false;
        String emailFarmacia = Utils.getEmail();
        StringTokenizer st=new StringTokenizer(emailFarmacia,"@");
        String partitaIva=st.nextToken();
        Farmacia farmacia=farmaciaRepository.findByPartitaIva(partitaIva);
        Carrello carrello=farmacia.getCarrello();

        DettaglioCarrello dc=null;
        Collection<DettaglioCarrello> listaDC = carrello.getDettaglioCarrello();
        Iterator<DettaglioCarrello> it= listaDC.iterator();

        while(it.hasNext() && !giaInCarrello) {
            dc = it.next();
            if (dc.getProdotto().getId() == idProdotto) {
                dc.setQuantita(dc.getQuantita() + quantita);
                giaInCarrello = true;
            }
        }

        if(!giaInCarrello) {
            dc=new DettaglioCarrello();
            dc.setProdotto(prodotto);
            dc.setQuantita(quantita);
            carrello.getDettaglioCarrello().add(dc);
            dettaglioCarrelloRepository.save(dc);
        }
        return carrello;
    }

    @Transactional(rollbackFor = QuantitaInsufficienteException.class)
    public Carrello modificaCarrello(Long idProdotto, Integer quantita) throws QuantitaInsufficienteException{
        Prodotto prodotto=prodottoRepository.findById(idProdotto);
        boolean trovatoDC=false;

        String emailFarmacia = Utils.getEmail();
        StringTokenizer st=new StringTokenizer(emailFarmacia,"@");
        String partitaIva=st.nextToken();
        Farmacia farmacia=farmaciaRepository.findByPartitaIva(partitaIva);
        Carrello carrello=farmacia.getCarrello();

        DettaglioCarrello dc=null;
        Collection<DettaglioCarrello> listaDC = carrello.getDettaglioCarrello();
        Iterator<DettaglioCarrello> it= listaDC.iterator();

        while(it.hasNext() && !trovatoDC) {
            dc = it.next();
            if (dc.getProdotto().getId() == idProdotto) {
                if(quantita==0) { //vuoi eliminare dal carrello
                    dc.setQuantita(quantita);
                    trovatoDC=true;
                }
                else
                    if(quantita > 0 && dc.getProdotto().getQtaInStock() < dc.getQuantita()+quantita)
                        throw new QuantitaInsufficienteException(dc.getProdotto().getId());
                    else {
                        dc.setQuantita(dc.getQuantita() + quantita);
                        trovatoDC = true;
                }
            }
        }

        if(dc.getQuantita()==0) {
            listaDC.remove(dc);
            dettaglioCarrelloRepository.delete(dc);
        }
        //dettaglioCarrelloRepository.save(dc);
        return carrello;
    }


}
