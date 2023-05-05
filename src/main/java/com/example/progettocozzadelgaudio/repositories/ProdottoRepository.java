package com.example.progettocozzadelgaudio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.progettocozzadelgaudio.entities.Prodotto;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto,Integer> {

    List<Prodotto> findByNome(String nome);

    List<Prodotto> findByNomeContaining(String nome);
    List<Prodotto> findByPrincipioAttivo(String principioAttivo);

    Prodotto findById(Long productId);

}
