package br.com.cadeira.controle.vitrium.vitrium.repositories;

import br.com.cadeira.controle.vitrium.vitrium.entity.Cadeiras;
import br.com.cadeira.controle.vitrium.vitrium.entity.enums.ECadeira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CadeiraRepository extends JpaRepository<Cadeiras, Long> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Cadeiras c " +
            "WHERE c.cadeira = :cadeira " +
            "AND c.devolvida = false " +
            "AND c.dtDevolucao IS NULL")
    boolean existsByCadeiraAndNaoDevolvida(@Param("cadeira") ECadeira cadeira);
    
}
