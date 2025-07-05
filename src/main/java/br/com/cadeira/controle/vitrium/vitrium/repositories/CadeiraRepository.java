package br.com.cadeira.controle.vitrium.vitrium.repositories;

import br.com.cadeira.controle.vitrium.vitrium.entity.Cadeiras;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadeiraRepository extends JpaRepository<Cadeiras, Long>{
    
}
