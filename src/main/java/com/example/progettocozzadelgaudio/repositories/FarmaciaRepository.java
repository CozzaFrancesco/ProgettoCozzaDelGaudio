package com.example.progettocozzadelgaudio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.progettocozzadelgaudio.entities.Farmacia;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FarmaciaRepository extends JpaRepository<Farmacia,Integer> {

    List<Farmacia> findByNome(String nome);

    List<Farmacia> findByCitta(String citta);

    List<Farmacia> findByCittaAndIndirizzo(String citta, String indirizzo);

    boolean existsByPartitaIva(String pIva);

}
