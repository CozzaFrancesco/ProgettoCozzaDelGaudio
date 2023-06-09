package com.example.progettocozzadelgaudio.services;

import com.example.progettocozzadelgaudio.repositories.ProdottoRepository;
import com.example.progettocozzadelgaudio.support.exception.AggiornamentoFallitoException;
import com.example.progettocozzadelgaudio.support.exception.ProdottoGiaEsistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import com.example.progettocozzadelgaudio.entities.Prodotto;


@Service
public class ProdottoService {

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Transactional(readOnly = true)
    public List<Prodotto> visualizzaTuttiProdotti(int numeroPagina, int dimensionePagina, String sortBy) {
        Pageable paging = PageRequest.of(numeroPagina, dimensionePagina, Sort.by(sortBy));
        Page<Prodotto> pagedResult = prodottoRepository.findAll(paging);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<Prodotto> trovaProdottiDalNome(String nome) {
        return prodottoRepository.findByNomeContaining(nome);
    }

    @Transactional(readOnly = true)
    public List<Prodotto> trovaProdottoDaPrincipioAttivo(String principioAttivo){
        return prodottoRepository.findByPrincipioAttivo(principioAttivo);
    }

    @Transactional(readOnly = true)
    public List<Prodotto> trovaProdottoConRicercaAvanzata(String nome, String principioAttivo, String formaFarmaceutica){
        return prodottoRepository.ricercaAvanzata(nome,principioAttivo,formaFarmaceutica);
    }

    //solo gestore
    @Transactional(readOnly = false, rollbackFor = ProdottoGiaEsistenteException.class)
    public Prodotto aggiungiProdotto(String nome, String principioAttivo, double prezzoUnitario, String formaFarmaceutica, Integer qtaInStock) throws ProdottoGiaEsistenteException {
        if(prodottoRepository.existsByNomeAndFormaFarmaceutica(nome,formaFarmaceutica))
            throw new ProdottoGiaEsistenteException();

        Prodotto prodotto= new Prodotto();
        prodotto.setNome(nome);
        prodotto.setFormaFarmaceutica(formaFarmaceutica);
        prodotto.setPrezzoUnitario(prezzoUnitario);
        prodotto.setPrincipioAttivo(principioAttivo);
        prodotto.setQtaInStock(qtaInStock);

        return prodottoRepository.save(prodotto);
    }

    @Transactional(readOnly = true)
    public Prodotto trovaProdottoDaId(Long id) {
        return prodottoRepository.findById(id);
    }

    //solo gestore
    @Transactional(readOnly = false, rollbackFor = AggiornamentoFallitoException.class)
    public Prodotto aggiornaProdotto(Long id, Integer quantita, double prezzoUnitario) throws AggiornamentoFallitoException{

        if(quantita < 0 || prezzoUnitario < 0)
            throw new AggiornamentoFallitoException();

        Prodotto prodotto= prodottoRepository.findByIdWithLock(id);
        prodotto.setPrezzoUnitario(prezzoUnitario);
        prodotto.setQtaInStock(prodotto.getQtaInStock()+quantita);
        //prodottoRepository.save(prodotto);
        return prodotto;
    }

    //TO-DO: Elimina prodotto

}
