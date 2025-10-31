package br.com.cadeira.controle.vitrium.common.configs.security;

import br.com.cadeira.controle.vitrium.authentication.entity.User;
import br.com.cadeira.controle.vitrium.authentication.entity.enums.UserRole;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TokenServiceTest {

    private TokenService tokenService;
    private final String TEST_SECRET = "test_secret_key";
    private final String TEST_ISSUER = "auth-api"; // Emissor (issuer) do token. É um "nome" para identificar quem está gerando o token.
    private User userTest;

    /* @BeforeEach roda antes de cada método @Test */
    @BeforeEach
    void setUp() {
        /* 1. Instancia o serviço manualmente */
        tokenService = new TokenService();

        /* 2. Injeta o 'secret' 'TEST_SECRET'*/
        // Isso substitui o trabalho do @Value("${api.security.token.secret}")
        ReflectionTestUtils.setField(
                tokenService,   // o objeto onde o valor será injetado
                "secret",       // O nome do campo (private String secret)
                TEST_SECRET     // O valor que queremos injetar
        );

        /* 3. Cria um usuário fictício */
        userTest = new User("testUser", "passwordTest", UserRole.USER);
    }

    @Test
    @DisplayName("Deve gerar um token corretamente e com as claims corretas")
    void deveGerarTokenCorretamenteClaimsCorretas() {
        // Act
        String token = tokenService.generateToken(userTest);

        // Assert
        // o token não pode ser nulo nem vazio
        assertThat(token).isNotBlank().isNotNull();

        // Decodifica o token para verificar o conteúdo (payload)
        var decodedJWT = JWT.require(Algorithm.HMAC256(TEST_SECRET))
                .withIssuer(TEST_ISSUER)
                .build()
                .verify(token);

        // Verifica o Subject 'Login'
        assertThat(decodedJWT.getSubject()).isEqualTo(userTest.getLogin());

        // Verifica o Issuer
        assertThat(decodedJWT.getIssuer()).isEqualTo(TEST_ISSUER);

        // Verifica a Claim 'roles'
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
        assertThat(roles).isEqualTo(List.of("ROLE_USER"));
    }

    @Test
    @DisplayName("Deve gerar um token JWT válido e retornar o subject")
    void deveGerarTokenJWTRetornarSubject() {
        // Arrange
        String token = tokenService.generateToken(userTest);

        // Act
        String subject = tokenService.validateToken(token);

        assertThat(subject).isEqualTo(userTest.getLogin());
    }

    @Test
    @DisplayName("Deve retornar string vazia ao validar token com assinatura inválida")
    void DeveRetornarStringVaziaAoValidarToken() {
        // Arrange
        // Gera um token com um secret diferente
        Algorithm algorithm = Algorithm.HMAC256("test_secret_key_diferente");
        String tokenInvalido = JWT.create()
                .withIssuer(TEST_ISSUER)
                .withSubject(userTest.getLogin())
                .withExpiresAt(Instant.now().plusSeconds(20))
                .sign(algorithm);

        // Act
        // O serviço tentara validar com o TEST_SECRET
        String subject = tokenService.validateToken(tokenInvalido);

        // Verificação
        assertThat(subject).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar string vazia ao validar token com issue incorreta")
    void DeveRetornarStringVaziaAoValidarTokenIssue() {
        // Arrange
        // Gera um token com issue diferente
        String tokenIssueInvalido = JWT.create()
                .withIssuer("issuer_invalido")
                .withSubject(userTest.getLogin())
                .withExpiresAt(Instant.now().plusSeconds(20))
                .sign(Algorithm.HMAC256(TEST_SECRET));


        // Act
        // O serviço tentara validar com ISSUER incorreto
        String subject = tokenService.validateToken(tokenIssueInvalido);

        // Verificação
        assertThat(subject).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar string vazia ao validar com token expirado")
    void DeveRetornarStringVaziaAoValidarTokenExpirado() {
        // Arrange
        // Gera um token a 2 minuto atrás
        String tokenInvalidoExpirado = JWT.create()
                .withIssuer(TEST_ISSUER)
                .withSubject(userTest.getLogin())
                .withExpiresAt(Instant.now().minusSeconds(120))
                .sign(Algorithm.HMAC256(TEST_SECRET));

        // Act
        // O serviço tentara validar com o token expirado
        String subject = tokenService.validateToken(tokenInvalidoExpirado);

        //Verificação
        assertThat(subject).isEmpty();
    }
}