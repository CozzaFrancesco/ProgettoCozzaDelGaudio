package com.example.progettocozzadelgaudio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.progettocozzadelgaudio.entities.Visita;

import java.util.Collection;

@Repository
public interface VisitaRepository extends JpaRepository<Visita,Integer> {

    Visita findById(Long id);
}
