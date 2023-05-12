package com.example.progettocozzadelgaudio.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.progettocozzadelgaudio.entities.Farmacia;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Collection;
import java.util.List;

@Repository
public interface FarmaciaRepository extends JpaRepository<Farmacia,Integer> {

    List<Farmacia> findByNome(String nome);

    List<Farmacia> findByCitta(String citta);

    List<Farmacia> findByCittaAndIndirizzo(String citta, String indirizzo);

    Farmacia findByPartitaIva(String partitaIva);

    List<Farmacia> findAllByCitta(String citta, Pageable paging);

    boolean existsByPartitaIva(String pIva);

    Farmacia findById(Long id);

}
