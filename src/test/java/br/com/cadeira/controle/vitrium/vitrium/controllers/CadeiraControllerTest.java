package br.com.cadeira.controle.vitrium.vitrium.controllers;

import br.com.cadeira.controle.vitrium.authentication.repository.UserRepository;
import br.com.cadeira.controle.vitrium.common.configs.security.SecurityConfigurations;
import br.com.cadeira.controle.vitrium.common.configs.security.TokenService;
import br.com.cadeira.controle.vitrium.vitrium.dto.AdicionaCadeiraDTO;
import br.com.cadeira.controle.vitrium.vitrium.dto.ListaCadeirasDTO;
import br.com.cadeira.controle.vitrium.vitrium.entity.Cadeiras;
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
import java.time.ZoneOffset;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    @DisplayName("POST /api/cadeira - Deve retornar 201 com Location apontando para o empréstimo salvo")
    @WithMockUser(roles = "ADMIN")
    void postCadeiraRetornaLocationComId() throws Exception {
        Cadeiras salva = new Cadeiras("Paciente Teste", "Clínica Teste", 101, ECadeira.CADEIRA_01);
        salva.setId(7L);
        salva.registraHorarioEntrega();
        when(cadeiraService.addCadeira(any())).thenReturn(salva);

        mockMvc.perform(
                        post("/api/cadeira")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        new AdicionaCadeiraDTO("Paciente Teste", "Clínica Teste", 101, ECadeira.CADEIRA_01)))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/cadeira/7")))
                .andExpect(jsonPath("$.cadeira", is("CADEIRA_01")))
                .andExpect(jsonPath("$.devolvida", is(false)));
    }

    @Test
    @DisplayName("POST /api/cadeira - Deve retornar 400 quando a cadeira do corpo não existe no enum")
    @WithMockUser(roles = "ADMIN")
    void postCadeiraComEnumInvalido() throws Exception {
        mockMvc.perform(
                        post("/api/cadeira")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"nomePaciente\":\"P\",\"destino\":\"D\",\"numeroClinica\":1,\"cadeira\":\"CADEIRA_99\"}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]", containsString("cadeira")));
    }

    @Test
    @DisplayName("GET /api/cadeira - Deve formatar as datas no horário de Brasília, mesmo vindas do banco em UTC")
    @WithMockUser(roles = "ADMIN")
    void getCadeirasFormataDataEmBrasilia() throws Exception {
        // 18:00 UTC = 15:00 em Brasília
        OffsetDateTime entregaUtc = OffsetDateTime.of(2026, 11, 2, 18, 0, 0, 0, ZoneOffset.UTC);
        when(cadeiraService.findAll()).thenReturn(List.of(new ListaCadeirasDTO(
                1L, "Paciente Teste", "Clínica Teste", 101, entregaUtc, null, ECadeira.CADEIRA_01, false)));

        mockMvc.perform(get("/api/cadeira"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dataEntrega", is("02-11-2026 15:00:00")));
    }

}