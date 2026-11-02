package br.com.cadeira.controle.vitrium.authentication.config;

import br.com.cadeira.controle.vitrium.authentication.entity.User;
import br.com.cadeira.controle.vitrium.authentication.entity.enums.UserRole;
import br.com.cadeira.controle.vitrium.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Cria o primeiro ADMIN na subida da aplicação, a partir de ADMIN_LOGIN e ADMIN_PASSWORD.
 * Só age quando ainda não existe nenhum usuário, já que o cadastro público não cria ADMIN.
 */
@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String login;
    private final String password;

    public AdminInitializer(UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            @Value("${api.admin.login:}") String login,
                            @Value("${api.admin.password:}") String password) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.login = login;
        this.password = password;
    }

    @Override
    public void run(String... args) {
        if (login.isBlank() || password.isBlank() || userRepository.count() > 0) {
            return;
        }
        userRepository.save(new User(login, passwordEncoder.encode(password), UserRole.ADMIN));
    }
}
