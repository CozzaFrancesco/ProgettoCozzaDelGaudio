package com.example.progettocozzadelgaudio.repositories;

import com.example.progettocozzadelgaudio.entities.Appuntamento;

import java.util.Collection;
import com.example.progettocozzadelgaudio.entities.Appuntamento;
import com.example.progettocozzadelgaudio.entities.Farmacia;
import java.time.LocalDate;
import com.example.progettocozzadelgaudio.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppuntamentoRepository extends JpaRepository<Appuntamento,Integer> {
    Collection<Appuntamento> findByCliente(Cliente cliente);

    Collection<Appuntamento> findByFarmaciaAndData(Farmacia farmacia, LocalDate data);


}
