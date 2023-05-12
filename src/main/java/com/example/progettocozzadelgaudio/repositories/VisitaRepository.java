package com.example.progettocozzadelgaudio.repositories;

import org.hibernate.LockMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.progettocozzadelgaudio.entities.Visita;

import javax.persistence.LockModeType;
import java.util.Collection;

@Repository
public interface VisitaRepository extends JpaRepository<Visita,Integer> {

    Visita findById(Long id);

    @Lock(LockModeType.PESSIMISTIC_READ) //to do cambiare tutto quello che serve con questo
    @Query("select v from Visita v where v.id=?1 ")
    Visita findByIdWithLock(Long id);
}
