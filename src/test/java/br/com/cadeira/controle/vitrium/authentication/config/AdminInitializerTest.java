package br.com.cadeira.controle.vitrium.authentication.config;

import br.com.cadeira.controle.vitrium.authentication.entity.User;
import br.com.cadeira.controle.vitrium.authentication.entity.enums.UserRole;
import br.com.cadeira.controle.vitrium.authentication.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminInitializerTest {

    @Mock
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("Cria o ADMIN com senha criptografada quando não há usuários")
    void criaAdminQuandoBancoVazio() {
        when(userRepository.count()).thenReturn(0L);

        new AdminInitializer(userRepository, passwordEncoder, "admin", "admin123").run();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User admin = captor.getValue();
        assertThat(admin.getLogin()).isEqualTo("admin");
        assertThat(admin.getRole()).isEqualTo(UserRole.ADMIN);
        assertThat(passwordEncoder.matches("admin123", admin.getPassword())).isTrue();
    }

    @Test
    @DisplayName("Não cria nada quando já existem usuários")
    void naoCriaQuandoJaHaUsuarios() {
        when(userRepository.count()).thenReturn(3L);

        new AdminInitializer(userRepository, passwordEncoder, "admin", "admin123").run();

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não cria nada quando ADMIN_LOGIN ou ADMIN_PASSWORD não foram definidos")
    void naoCriaSemCredenciais() {
        new AdminInitializer(userRepository, passwordEncoder, "", "").run();

        verify(userRepository, never()).save(any());
    }
}
