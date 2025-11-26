package br.com.cadeira.controle.vitrium.authentication.repository;

import br.com.cadeira.controle.vitrium.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByLogin(String login);
}
