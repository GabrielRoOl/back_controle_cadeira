package br.com.cadeira.controle.vitrium.authentication.controller;

import br.com.cadeira.controle.vitrium.authentication.repository.UserRepository;
import br.com.cadeira.controle.vitrium.common.configs.security.SecurityConfigurations;
import br.com.cadeira.controle.vitrium.common.configs.security.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * O cadastro é público, mas não pode ser usado para criar um ADMIN.
 */
@Import(SecurityConfigurations.class)
@WebMvcTest(controllers = AuthenticationController.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private TokenService tokenService;

    private String cadastro(String role) {
        return """
                {"login":"novo","password":"senha123","confirmPassword":"senha123","role":"%s"}
                """.formatted(role);
    }

    @Test
    @DisplayName("Visitante sem login consegue se cadastrar como USER")
    void visitanteCadastraUser() throws Exception {
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(cadastro("USER")))
                .andExpect(status().isOk());

        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("Visitante sem login não consegue se cadastrar como ADMIN")
    void visitanteNaoCadastraAdmin() throws Exception {
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(cadastro("ADMIN")))
                .andExpect(status().isForbidden());

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("USER logado não consegue cadastrar um ADMIN")
    @WithMockUser(roles = "USER")
    void userNaoCadastraAdmin() throws Exception {
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(cadastro("ADMIN")))
                .andExpect(status().isForbidden());

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("ADMIN logado consegue cadastrar outro ADMIN")
    @WithMockUser(roles = "ADMIN")
    void adminCadastraAdmin() throws Exception {
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(cadastro("ADMIN")))
                .andExpect(status().isOk());

        verify(userRepository).save(any());
    }
}
