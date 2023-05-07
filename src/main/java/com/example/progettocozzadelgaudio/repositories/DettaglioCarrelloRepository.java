package com.example.progettocozzadelgaudio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.progettocozzadelgaudio.entities.DettaglioCarrello;
import org.springframework.stereotype.Repository;

@Repository
public interface DettaglioCarrelloRepository extends JpaRepository<DettaglioCarrello,Integer> {
}
