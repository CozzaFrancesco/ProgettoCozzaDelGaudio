package com.example.progettocozzadelgaudio.services;

import com.example.progettocozzadelgaudio.authentication.Utils;
import com.example.progettocozzadelgaudio.entities.Appuntamento;
import com.example.progettocozzadelgaudio.entities.Visita;
import com.example.progettocozzadelgaudio.entities.Cliente;
import com.example.progettocozzadelgaudio.entities.Farmacia;
import com.example.progettocozzadelgaudio.repositories.AppuntamentoRepository;
import com.example.progettocozzadelgaudio.repositories.ClienteRepository;
import com.example.progettocozzadelgaudio.repositories.FarmaciaRepository;
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
    public Appuntamento creaAppuntamento(Farmacia farmacia, Visita visita, int anno, int mese, int giorno, int ora, int minuti ) throws AppuntamentoNonPiuDisponibileException {
        String emailCliente = Utils.getEmail();
        StringTokenizer st=new StringTokenizer("@");
        String codiceFiscale=st.nextToken();
        Cliente cliente=clienteRepository.findByCodiceFiscale(codiceFiscale);

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

}
