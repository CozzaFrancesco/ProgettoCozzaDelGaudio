package com.example.progettocozzadelgaudio.services;

import com.example.progettocozzadelgaudio.authentication.Utils;
import com.example.progettocozzadelgaudio.entities.Appuntamento;
import com.example.progettocozzadelgaudio.entities.Visita;
import com.example.progettocozzadelgaudio.entities.Cliente;
import com.example.progettocozzadelgaudio.entities.Farmacia;
import com.example.progettocozzadelgaudio.repositories.AppuntamentoRepository;
import com.example.progettocozzadelgaudio.repositories.ClienteRepository;
import com.example.progettocozzadelgaudio.repositories.FarmaciaRepository;
import com.example.progettocozzadelgaudio.repositories.VisitaRepository;
import com.example.progettocozzadelgaudio.support.exception.AppuntamentoNonDisdicibileException;
import com.example.progettocozzadelgaudio.support.exception.AppuntamentoNonPiuDisponibileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;
import java.time.LocalDate;



import java.util.Collection;
import java.util.StringTokenizer;

@Service
public class AppuntamentoService {

    @Autowired
    private FarmaciaRepository farmaciaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AppuntamentoRepository appuntamentoRepository;

    @Autowired
    private VisitaRepository visitaRepository;

    private boolean eDisponibile(Farmacia farmacia, Visita visita, int anno, int mese, int giorno, int ora, int minuti) {
        LocalDate data=LocalDate.of(anno,mese,giorno);
        Collection<Appuntamento> listaApp=appuntamentoRepository.findByFarmaciaAndData(farmacia,data);
        LocalTime orario=LocalTime.of(ora,minuti);

        int contVisiteXorario=0;

        for(Appuntamento app:listaApp) {
            LocalTime orarioFineAppCorrente=app.getOrario().plusMinutes(app.getVisita().getDurata());
            if(app.getOrario().isBefore(orario) && orarioFineAppCorrente.isAfter(orario))
                contVisiteXorario++;
        }
        if(contVisiteXorario > farmacia.getNumDipendenti()-1)
            return false;
        return true;
    }

    @Transactional
    public Appuntamento creaAppuntamento(Long idFarmacia, Long idVisita, int anno, int mese, int giorno, int ora, int minuti ) throws AppuntamentoNonPiuDisponibileException {
        String emailCliente = Utils.getEmail();
        StringTokenizer st=new StringTokenizer("@");
        String codiceFiscale=st.nextToken();
        Cliente cliente=clienteRepository.findByCodiceFiscale(codiceFiscale);

        Farmacia farmacia= farmaciaRepository.findById(idFarmacia);
        Visita visita= visitaRepository.findById(idVisita);

        if(!eDisponibile(farmacia,visita,anno,mese,giorno,ora,minuti))
            throw new AppuntamentoNonPiuDisponibileException();

        Appuntamento appuntamento=new Appuntamento();
        appuntamento.setCliente(cliente);
        appuntamento.setFarmacia(farmacia);
        appuntamento.setData(LocalDate.of(anno,mese,giorno));
        appuntamento.setOrario(LocalTime.of(ora,minuti));
        appuntamento.setVisita(visita);

        return appuntamentoRepository.save(appuntamento);
    }

    @Transactional
    public void discidiAppuntamento(Long idAppuntamento) throws AppuntamentoNonDisdicibileException{
        LocalDate dataOdierna=LocalDate.now();
        Appuntamento appuntamento=appuntamentoRepository.findById(idAppuntamento);
        if(appuntamento.getData().equals(dataOdierna)
                || appuntamento.getData().isBefore(dataOdierna)
                || appuntamento.getData().equals(dataOdierna.plusDays(1)))
                throw new AppuntamentoNonDisdicibileException();

        appuntamentoRepository.delete(appuntamento);
    }
}
