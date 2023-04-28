package com.example.progettocozzadelgaudio.repositories;

import com.example.progettocozzadelgaudio.entities.Farmacia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.progettocozzadelgaudio.entities.Carrello;

@Repository
public interface CarrelloRepository extends JpaRepository<Carrello,Integer> {

    Carrello findByFarmacia(Farmacia f);

}
