package com.example.progettocozzadelgaudio.repositories;

import com.example.progettocozzadelgaudio.entities.Visita;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.progettocozzadelgaudio.entities.Farmacia;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
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

    @Lock(LockModeType.PESSIMISTIC_READ) //to do cambiare tutto quello che serve con questo
    @Query("select f from Farmacia f where f.id=?1 ")
    Farmacia findByIdWithLock(Long id);

}
