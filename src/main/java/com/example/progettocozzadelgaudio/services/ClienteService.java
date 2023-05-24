package com.example.progettocozzadelgaudio.services;

import com.example.progettocozzadelgaudio.authentication.Utils;
import com.example.progettocozzadelgaudio.entities.*;
import com.example.progettocozzadelgaudio.repositories.*;
import com.example.progettocozzadelgaudio.support.exception.DataNonValidaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

@Service
@Transactional(readOnly = true)
public class ClienteService {

    @Autowired
    private FarmaciaRepository farmaciaRepository;

    @Autowired
    private VisitaRepository visitaRepository;

    @Autowired
    private AppuntamentoRepository appuntamentoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private DettaglioMagazzinoRepository dettaglioMagazzinoRepository;

    private static final int PASSO_STANDARD=30; //minuti standard di cui si muove il cursore


    public List<Farmacia> visualizzaFarmacie(int numeroPagina, int dimensionePagina, String sortBy) {
        Pageable paging = PageRequest.of(numeroPagina, dimensionePagina, Sort.by(sortBy));
        Page<Farmacia> pagedResult = farmaciaRepository.findAll(paging);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }


    public Collection<Farmacia> visualizzaFarmaciePerCitta(String citta) {
        List<Farmacia> risultato = farmaciaRepository.findAllByCitta(citta);
        if ( !risultato.isEmpty() ) {
            return risultato;
        }
        else {
            return new ArrayList<>();
        }
    }


    public Magazzino visualizzaMagazzinoPerFarmacia(Long idFarmacia) {
        Farmacia farmacia=farmaciaRepository.findById(idFarmacia);
        return farmacia.getMagazzino();
    }


    public Collection<DettaglioMagazzino> visualizzaMagazzinoPerFarmaciaENomeProdotto(Long idFarmacia, String nome) {
        Farmacia farmacia=farmaciaRepository.findById(idFarmacia);
        Collection<DettaglioMagazzino> ret = new ArrayList<>();
        Collection<DettaglioMagazzino> listaDM= farmacia.getMagazzino().getDettaglioMagazzino();
        for(DettaglioMagazzino dm: listaDM )
            if(dm.getProdotto().getNome().equals(nome))
                ret.add(dm);
        return ret;
    }


    public Collection<Visita> visualizzaVisitePerFarmacia(Long idFarmacia) {
        Farmacia farmacia=farmaciaRepository.findById(idFarmacia);
        return farmacia.getVisite();
    }


    public Collection<LocalTime> visualizzaOrariDisponibiliPerVisitainFarmaciaEData(Long idFarmacia, Long idVisita,
                                                                                   int anno, int mese, int giorno)
                                                                                    throws DataNonValidaException {
        LocalDate dataOdierna = LocalDate.now();
        LocalDate data = LocalDate.of(anno,mese,giorno);
        if(! data.isAfter(dataOdierna))  //non posso prenotare per il giorno stesso e nemmeno per date passate
            throw new DataNonValidaException();

        Collection<LocalTime> ret=new ArrayList<>();
        Farmacia farmacia= farmaciaRepository.findById(idFarmacia);
        Visita visita= visitaRepository.findById(idVisita);
        int durataVisita= visita.getDurata(); //in minuti

        Collection<Appuntamento> visiteInData = appuntamentoRepository.findByFarmaciaAndData(farmacia,data);

        LocalTime cursore=farmacia.getOrarioInizioVisite();

        int passo=Integer.min(durataVisita,PASSO_STANDARD);

        while(cursore.compareTo(farmacia.getOrarioFineVisite().minusMinutes(durataVisita))<=0) {
            Collection<Appuntamento> visiteIR=visiteInRange(visiteInData,cursore,cursore.plusMinutes(durataVisita));
            if(visiteIR.size() < farmacia.getNumDipendenti()-1)
                ret.add(cursore);
            cursore=cursore.plusMinutes(passo);
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


    public Collection<Appuntamento> visualizzaAppuntamenti() {
        String emailCliente = Utils.getEmail();
        StringTokenizer st=new StringTokenizer(emailCliente,"@");
        String codiceFiscale=st.nextToken();
        Cliente cliente=clienteRepository.findByCodiceFiscale(codiceFiscale);
        return appuntamentoRepository.findByCliente(cliente);
    }


    public Cliente visualizzaCliente(){
        String emailCliente = Utils.getEmail();
        StringTokenizer st=new StringTokenizer(emailCliente,"@");
        String codiceFiscale=st.nextToken();
        return clienteRepository.findByCodiceFiscale(codiceFiscale);
    }

    @Transactional(readOnly = false)
    public Cliente modificaCitta(String citta){
        String emailCliente = Utils.getEmail();
        StringTokenizer st=new StringTokenizer(emailCliente,"@");
        String codiceFiscale=st.nextToken();
        Cliente cliente= clienteRepository.findByCodiceFiscale(codiceFiscale);
        cliente.setCitta(citta);
        //clienteRepository.save(cliente);
        return cliente;
    }

    @Transactional(readOnly = false)
    public Cliente modificaIndirizzo(String indirizzo){
        String emailCliente = Utils.getEmail();
        StringTokenizer st=new StringTokenizer(emailCliente,"@");
        String codiceFiscale=st.nextToken();
        Cliente cliente= clienteRepository.findByCodiceFiscale(codiceFiscale);
        cliente.setIndirizzo(indirizzo);
        //clienteRepository.save(cliente);
        return cliente;
    }
}

