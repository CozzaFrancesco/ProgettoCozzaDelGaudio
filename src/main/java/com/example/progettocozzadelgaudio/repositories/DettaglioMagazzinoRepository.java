package com.example.progettocozzadelgaudio.repositories;

import com.example.progettocozzadelgaudio.entities.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.progettocozzadelgaudio.entities.DettaglioMagazzino;

import java.util.List;

public interface DettaglioMagazzinoRepository extends JpaRepository<DettaglioMagazzino,Integer> {

    List<DettaglioMagazzino> findByProdotto(Prodotto p);

}
