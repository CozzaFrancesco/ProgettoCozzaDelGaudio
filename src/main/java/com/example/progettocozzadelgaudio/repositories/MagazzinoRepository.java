package com.example.progettocozzadelgaudio.repositories;

import com.example.progettocozzadelgaudio.entities.DettaglioMagazzino;
import com.example.progettocozzadelgaudio.entities.Magazzino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MagazzinoRepository extends JpaRepository<Magazzino,Integer> {

    Magazzino findByDettaglioMagazzino(DettaglioMagazzino dm);

}
