package com.example.progettocozzadelgaudio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.progettocozzadelgaudio.entities.Prodotto;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto,Integer> {

    List<Prodotto> findByNome(String nome);

    List<Prodotto> findByNomeContaining(String nome);
    List<Prodotto> findByPrincipioAttivo(String principioAttivo);

    Prodotto findById(Long productId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select p from Prodotto p where p.id=?1")
    Prodotto findByIdWithLock(Long id);


    boolean existsByNomeAndFormaFarmaceutica(String nome,String formaFarmaceutica);

    @Query("SELECT p " +
            "FROM Prodotto p " +
            "WHERE (p.nome LIKE :nome OR :nome IS NULL) AND " +
            "      (p.formaFarmaceutica LIKE : formaFarmaceutica OR :formaFarmaceutica IS NULL ) AND " +
            "      (p.principioAttivo LIKE : principioAttivo OR :principioAttivo IS NULL ) ")
    List<Prodotto> ricercaAvanzata(String nome, String principioAttivo, String formaFarmaceutica);


}
