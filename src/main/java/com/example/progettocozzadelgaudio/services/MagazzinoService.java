package com.example.progettocozzadelgaudio.services;

import com.example.progettocozzadelgaudio.authentication.Utils;
import com.example.progettocozzadelgaudio.entities.DettaglioCarrello;
import com.example.progettocozzadelgaudio.entities.DettaglioMagazzino;
import com.example.progettocozzadelgaudio.entities.Farmacia;
import com.example.progettocozzadelgaudio.entities.Magazzino;
import com.example.progettocozzadelgaudio.repositories.FarmaciaRepository;
import com.example.progettocozzadelgaudio.repositories.MagazzinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

@Service
@Transactional(readOnly = true)
public class MagazzinoService {

    @Autowired
    private FarmaciaRepository farmaciaRepository;

    @Autowired
    private MagazzinoRepository magazzinoRepository;

    private Farmacia visualizzaFarmacia() {
        String emailFarmacia = Utils.getEmail();
        StringTokenizer st=new StringTokenizer(emailFarmacia,"@");
        String partitaIva=st.nextToken();
        Farmacia farmacia=farmaciaRepository.findByPartitaIva(partitaIva);
        return farmacia;
    }


    public Collection<DettaglioMagazzino> visualizzaMagazzino() {
        Farmacia farmacia=visualizzaFarmacia();
        Magazzino magazzino= farmacia.getMagazzino();
        return magazzino.getDettaglioMagazzino();
    }

    public Collection<DettaglioMagazzino> visualizzaMagazzinoPerPrincipioAttivo(String principioAttivo){
        Farmacia farmacia=visualizzaFarmacia();
        Magazzino magazzino= farmacia.getMagazzino();
        Collection<DettaglioMagazzino> listaDM= magazzino.getDettaglioMagazzino();
        Collection<DettaglioMagazzino> risultato=new ArrayList<>();
        for(DettaglioMagazzino dm: listaDM)
            if(dm.getProdotto().getPrincipioAttivo().equals(principioAttivo))
                risultato.add(dm);
        return risultato;
    }

    public Collection<DettaglioMagazzino> visualizzaMagazzinoPerNome(String nome){
        Farmacia farmacia=visualizzaFarmacia();
        Magazzino magazzino= farmacia.getMagazzino();
        Collection<DettaglioMagazzino> listaDM= magazzino.getDettaglioMagazzino();
        Collection<DettaglioMagazzino> risultato=new ArrayList<>();
        for(DettaglioMagazzino dm: listaDM)
            if(dm.getProdotto().getNome().equals(nome))
                risultato.add(dm);
        return risultato;
    }

}
