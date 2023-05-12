package com.example.progettocozzadelgaudio.repositories;

import com.example.progettocozzadelgaudio.entities.DettaglioMagazzino;
import com.example.progettocozzadelgaudio.entities.Magazzino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface MagazzinoRepository extends JpaRepository<Magazzino,Integer> {

    Magazzino findByDettaglioMagazzino(DettaglioMagazzino dm);

}
