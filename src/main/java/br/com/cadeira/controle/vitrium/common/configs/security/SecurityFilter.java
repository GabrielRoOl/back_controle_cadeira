package br.com.cadeira.controle.vitrium.common.configs.security;
import br.com.cadeira.controle.vitrium.authentication.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Marca esta classe como um Componente do Spring (para que possamos injetá-la)
public class SecurityFilter extends OncePerRequestFilter { // Garante que o filtro rode apenas UMA VEZ por requisição

    @Autowired
    private TokenService tokenService; // Nosso serviço de token

    @Autowired
    private UserRepository userRepository; // O repositório para buscar o usuário no banco

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Tenta recuperar o token do cabeçalho da requisição
        var token = this.recoverToken(request);

        // 2. Se um token foi encontrado...
        if (token != null) {
            // 3. Valida o token usando nosso TokenService
            var login = tokenService.validateToken(token); // O 'login' é o "subject" do token

            // 4. Se o token for válido (login não está vazio)...
            if (!login.isEmpty()) {
                // 5. Busca os detalhes do usuário no banco de dados
                // Isso é importante para ter as permissões (roles) atualizadas
                UserDetails user = userRepository.findByLogin(login);

                if (user != null) {
                    // 6. Cria um objeto de autenticação que o Spring entende
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    // 7. Salva essa autenticação no Contexto de Segurança do Spring
                    // ESTA É A LINHA MÁGICA!
                    // A partir daqui, o Spring considera o usuário como "logado" para esta requisição.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        // 8. Independentemente de ter ou não um token, continua a cadeia de filtros.
        // Se o SecurityContext não foi preenchido, o Spring (na próxima etapa)
        // vai barrar o acesso aos endpoints protegidos.
        filterChain.doFilter(request, response);
    }

    /**
     * Método auxiliar para extrair o token do cabeçalho "Authorization".
     */
    private String recoverToken(HttpServletRequest request) {
        // Pega o valor do cabeçalho "Authorization"
        var authHeader = request.getHeader("Authorization");

        // Se o cabeçalho não existir, retorna nulo
        if (authHeader == null) {
            return null;
        }

        // O cabeçalho vem no formato "Bearer <token>"
        // Este replace remove o "Bearer " e deixa apenas o token
        return authHeader.replace("Bearer ", "");
    }
}