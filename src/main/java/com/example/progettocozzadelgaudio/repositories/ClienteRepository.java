package com.example.progettocozzadelgaudio.repositories;

import com.example.progettocozzadelgaudio.entities.Farmacia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.progettocozzadelgaudio.entities.Cliente;


import java.util.Collection;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Integer> {

    Cliente findByCodiceFiscale(String codiceFiscale);

    boolean existsByCodiceFiscale(String codiceFiscale);

    Cliente findById(Long id);
}
