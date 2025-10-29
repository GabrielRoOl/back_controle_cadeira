package br.com.cadeira.controle.vitrium.common.configs.security;
import br.com.cadeira.controle.vitrium.authentication.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Service // Anotação que marca esta classe como um Serviço (onde fica a lógica de negócio)
public class TokenService {

    // Injeta o valor da propriedade 'api.security.token.secret'
    // que está definida no seu arquivo 'application.properties'.
    // Esta é a sua CHAVE SECRETA. Não a exponha!
    @Value("${api.security.token.secret}")
    private String secret;

    // Emissor (issuer) do token. É um "nome" para identificar quem está gerando o token.
    private static final String ISSUER = "auth-api";

    /**
     * Gera um Token JWT para um usuário autenticado.
     */
    public String generateToken(User user) {
        try {
            // Define o algoritmo de assinatura (HMAC256) usando nossa chave secreta
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    // Define o emissor (quem criou o token)
                    .withIssuer(ISSUER)

                    // Define o "assunto" (subject) do token, geralmente o login ou ID do usuário
                    .withSubject(user.getLogin())

                    // Adiciona "claims" personalizadas. Aqui, estamos salvando as roles do usuário
                    // dentro do próprio token. Isso é útil para verificações de autorização.
                    .withClaim("roles", user.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))

                    // Define a data de expiração do token
                    .withExpiresAt(getExpirationDate())

                    // Assina o token com o algoritmo e a chave secreta
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            // Captura exceções que podem ocorrer durante a criação do token
            throw new RuntimeException("Erro ao criar Token JWT: " + e.getMessage(), e);
        }
    }

    /**
     * Valida um Token JWT e retorna o "assunto" (subject) se o token for válido.
     */
    public String validateToken(String token) {
        try {
            // Define o algoritmo (o mesmo usado na geração)
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    // Configura o validador para aceitar apenas tokens do nosso emissor
                    .withIssuer(ISSUER)

                    // Constrói o verificador
                    .build()

                    // Verifica o token (isso checa a assinatura, a expiração e o emissor)
                    .verify(token)

                    // Se o token for válido, extrai o "assunto" (o login do usuário)
                    .getSubject();
        } catch (JWTVerificationException e) {
            // Se a verificação falhar (token inválido, expirado, etc.),
            // retorna uma string vazia para indicar falha na validação.
            return "";
        }
    }

    /**
     * Método auxiliar para calcular a data de expiração do token.
     * (Ex: 2 horas a partir de agora, no fuso horário -03:00)
     */
    private Instant getExpirationDate() {
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}