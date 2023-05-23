package com.example.progettocozzadelgaudio.services;

import com.example.progettocozzadelgaudio.authentication.Utils;
import com.example.progettocozzadelgaudio.entities.Appuntamento;
import com.example.progettocozzadelgaudio.entities.Farmacia;
import com.example.progettocozzadelgaudio.entities.Prodotto;
import com.example.progettocozzadelgaudio.entities.Visita;
import com.example.progettocozzadelgaudio.repositories.AppuntamentoRepository;
import com.example.progettocozzadelgaudio.repositories.FarmaciaRepository;
import com.example.progettocozzadelgaudio.repositories.VisitaRepository;
import com.example.progettocozzadelgaudio.support.exception.AggiornamentoFallitoException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class FarmaciaService {

    @Autowired
    private FarmaciaRepository farmaciaRepository;

    @Autowired
    private AppuntamentoRepository appuntamentoRepository;

    @Autowired
    private VisitaRepository visitaRepository;

    //solo gestore
    @Transactional
    public Farmacia aggiornaBudget(Long id, Double budgetAggiuntivo) throws AggiornamentoFallitoException {
        if(budgetAggiuntivo<0)
            throw new AggiornamentoFallitoException();
        Farmacia farmacia=farmaciaRepository.findById(id);
        farmacia.setBudget(farmacia.getBudget()+budgetAggiuntivo);
        return farmaciaRepository.save(farmacia);
    }

    @Transactional
    public Farmacia visualizzaFarmacia() {
        String emailFarmacia = Utils.getEmail();
        StringTokenizer st=new StringTokenizer(emailFarmacia,"@");
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

    //solo gestore
    @Transactional
    public Farmacia aggiungiDipendenti(Long idFarmacia, int dipendentiDaAggiungere) throws AggiornamentoFallitoException{
        if(dipendentiDaAggiungere<0)
            throw new AggiornamentoFallitoException();
        Farmacia farmacia= farmaciaRepository.findById(idFarmacia);
        farmacia.setNumDipendenti(farmacia.getNumDipendenti()+dipendentiDaAggiungere);
        return farmaciaRepository.save(farmacia);
    }

    //solo gestore
    @Transactional
    public Farmacia rimuoviDipendenti(Long idFarmacia, int dipendentiDaRimuovere) throws AggiornamentoFallitoException{
        Farmacia farmacia= farmaciaRepository.findById(idFarmacia);

        if(dipendentiDaRimuovere<0
                || farmacia.getNumDipendenti()<=dipendentiDaRimuovere
                || !ePossibileRimuovereDipendenti(farmacia,dipendentiDaRimuovere))
            throw new AggiornamentoFallitoException();

        farmacia.setNumDipendenti(farmacia.getNumDipendenti()-dipendentiDaRimuovere);
        return farmaciaRepository.save(farmacia);
    }

    private boolean ePossibileRimuovereDipendenti(Farmacia farmacia, int dipendentiDaRimuovere) {
        //bisogna capire se, eliminando i dipendenti, qualche appuntamento rimane scoperto
        LocalDate today=LocalDate.now();
        Collection<Appuntamento> listaAppuntamenti=appuntamentoRepository.findByFarmaciaAndDataAfter(farmacia,today);

        int dipendentiDopoRimozionePerVisite=farmacia.getNumDipendenti()-dipendentiDaRimuovere-1;

        List<LocalDate> listaDate=new LinkedList<>();
        for(Appuntamento app:listaAppuntamenti) {
            if(!listaDate.contains(app.getData())) {
                List<Appuntamento> appPerData=appuntamentoRepository.findByFarmaciaAndData(farmacia,app.getData());
                if (!appuntamentiContemporaneiMassimiPerData(appPerData,dipendentiDopoRimozionePerVisite))
                    return false;
                listaDate.add(app.getData());
            }
        }
        return true;
    }

    private boolean appuntamentiContemporaneiMassimiPerData(List<Appuntamento> listaAppPerData, int dipendentiDopoRimozionePerVisite) {

        for(int i=0;i<listaAppPerData.size();i++) {
            int contContemporanei=0;
            for(int j=0;j<listaAppPerData.size() && i!=j; j++)
                if( sonoAppuntamentiContemporanei(listaAppPerData.get(i),listaAppPerData.get(j)))
                    if(contContemporanei==0)
                        contContemporanei=2;
                    else
                        contContemporanei++;
            if(contContemporanei > dipendentiDopoRimozionePerVisite)
                return false;
        }
        return true;
    }

    private boolean sonoAppuntamentiContemporanei(Appuntamento ap1, Appuntamento ap2) {
        LocalTime inizioPrimoApp=ap1.getOrario();
        LocalTime finePrimoApp=inizioPrimoApp.plusMinutes(ap1.getVisita().getDurata());

        LocalTime inizioSecondoApp=ap2.getOrario();
        LocalTime fineSecondoApp=inizioSecondoApp.plusMinutes(ap2.getVisita().getDurata());

        if (inizioPrimoApp.equals(inizioSecondoApp)
                || (inizioPrimoApp.isAfter(inizioSecondoApp)) && finePrimoApp.isBefore(fineSecondoApp)
                || (inizioPrimoApp.isBefore(inizioSecondoApp)) && ( finePrimoApp.isAfter(fineSecondoApp) || finePrimoApp.equals(fineSecondoApp)))
            return true;
        return false;
    }

    //solo farmacia
    @Transactional
    public Collection<Appuntamento> visualizzaAppuntamenti(int anno, int mese, int giorno) {
        Farmacia farmacia=visualizzaFarmacia();
        return appuntamentoRepository.findByFarmaciaAndData(farmacia,LocalDate.of(anno,mese,giorno));
    }

    @Transactional
    public Collection<Visita> visualizzaVisiteOfferte(){
        Farmacia farmacia=visualizzaFarmacia();
        return farmacia.getVisite();
    }

    @Transactional
    public Collection<Visita> visualizzaVisiteAggiungibili(){
        Farmacia farmacia = visualizzaFarmacia();
        Collection<Visita> visiteFarmacia= farmacia.getVisite();
        Collection<Visita>  tutteLeVisite = visitaRepository.findAll();
        Collection<Visita> ret= new ArrayList<>();
        for(Visita v: tutteLeVisite)
            if(!visiteFarmacia.contains(v))
                ret.add(v);
        return ret;
    }

    @Transactional
    public Farmacia aggiungiVisita(Long idVisita){
        Farmacia farmacia = visualizzaFarmacia();
        Visita visita= visitaRepository.findById(idVisita);
        if(!farmacia.getVisite().contains(visita))
            farmacia.getVisite().add(visita);
        farmaciaRepository.save(farmacia);
        return farmacia;
    }

}
