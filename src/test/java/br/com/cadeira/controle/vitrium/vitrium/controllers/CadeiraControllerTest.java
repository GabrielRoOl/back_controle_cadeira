package br.com.cadeira.controle.vitrium.vitrium.controllers;

import br.com.cadeira.controle.vitrium.authentication.repository.UserRepository;
import br.com.cadeira.controle.vitrium.common.configs.security.SecurityConfigurations;
import br.com.cadeira.controle.vitrium.common.configs.security.TokenService;
import br.com.cadeira.controle.vitrium.vitrium.dto.ListaCadeirasDTO;
import br.com.cadeira.controle.vitrium.vitrium.entity.enums.ECadeira;
import br.com.cadeira.controle.vitrium.vitrium.servicies.CadeiraService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * @WebMvcTest foca APENAS na camada web.
 * (controllers = CadeiraController.class) carrega nosso controller.
 * Isso TAMBÉM carrega SecurityConfigurations e SecurityFilter.
 */
@Import(SecurityConfigurations.class) // Força o @WebMvcTest a carregar o bean SecurityFilterChain personalizado.
@WebMvcTest(controllers = CadeiraController.class)
class CadeiraControllerTest {

    @Autowired
    private MockMvc mockMvc; // Para simular requisições

    @Autowired
    private ObjectMapper objectMapper; // Para converter DTOs -> JSON

    @MockitoBean
    private CadeiraService cadeiraService;
    @MockitoBean
    private TokenService tokenService;
    @MockitoBean
    private UserRepository userRepository;

    @Test
    @DisplayName("GET /api/cadeira - Deve retornar 200 OK e lista de cadeiras com ROLE_ADMIN")
    @WithMockUser(roles = "ADMIN")
        // Simula um usuário 'ADMIN' logado
    void getCadeiras() throws Exception {
        // Arrange
        // MOCKA DOIS ITENS PARA SEREM USADOS NO GET
        ListaCadeirasDTO cadeira1 = new ListaCadeirasDTO(
                1L,
                "Gabriel Teste",
                "Neo",
                1,
                OffsetDateTime.now(),
                OffsetDateTime.now().plusSeconds(25),
                ECadeira.CADEIRA_04,
                true);

        ListaCadeirasDTO cadeira2 = new ListaCadeirasDTO(
                2L,
                "Gabriel Teste",
                "Nefrostar",
                1,
                OffsetDateTime.now(),
                OffsetDateTime.now().plusSeconds(25),
                ECadeira.CADEIRA_01,
                true);

        List<ListaCadeirasDTO> listaCadeiras = List.of(cadeira1, cadeira2);

        // Ensine o mock do service
        when(cadeiraService.findAll()).thenReturn(listaCadeiras);

        mockMvc.perform(
                        get("/api/cadeira") // Endpoint
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                /* AS BIBLIOTECAS UTILIZADAS SERÃO AS

                    org.hamcrest.Matchers.hasSize
                    org.hamcrest.Matchers.is
                    org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
                 */

                .andExpect(jsonPath("$", hasSize(2))) // Verifica se a lista JSON tem 2 itens
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].destino", is("Neo")))
                .andExpect(jsonPath("$[0].devolvida", is(true)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].destino", is("Nefrostar")))
                .andExpect(jsonPath("$[1].devolvida", is(true)));

    }

    @Test
    @DisplayName("GET /api/cadeira - Deve retornar 200 OK e lista vazia com ROLE_ADMIN")
    @WithMockUser(roles = "ADMIN")
    void getCadeira() throws Exception {

        // Arrange
        // Ensina o mock a retornar uma lista vazia
        when(cadeiraService.findAll()).thenReturn(List.of());

        // Act and Assert
        mockMvc.perform(
                        get("/api/cadeira")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /api/cadeira - Deve retornar 403 Forbidden com ROLE_USER")
    @WithMockUser(roles = "USER")
        // Simula usuário comum
    void getCadeiraForbidden() throws Exception {

        // Arrange
        // Ensina o mock a retornar uma lista vazia
        when(cadeiraService.findAll()).thenReturn(List.of());

        // Act and Assert
        mockMvc.perform(
                        get("/api/cadeira")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                // Testa se a regra .hasRole("ADMIN") está funcionando
                .andExpect(status().isForbidden());
    }

}