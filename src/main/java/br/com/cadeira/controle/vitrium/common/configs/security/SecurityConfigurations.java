package br.com.cadeira.controle.vitrium.common.configs.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration // Anotação que indica ao Spring que esta é uma classe de configuração
@EnableWebSecurity // Habilita as configurações de segurança web do Spring Security
public class SecurityConfigurations {

    @Value("${cors.origins}")
    private String[] corsOrigins;

    // Injeta o filtro de segurança que criamos (SecurityFilter)
    // O Spring vai automaticamente encontrar o 'Bean' do SecurityFilter e colocá-lo aqui
    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Desabilita a proteção contra CSRF (Cross-Site Request Forgery)
                // Como usamos JWT (stateless), não precisamos dessa proteção baseada em cookies/sessão.
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())

                // Configura a política de gerenciamento de sessão como STATELESS (sem estado)
                // Isso é crucial para APIs REST com JWT. Dizemos ao Spring: "Não crie sessões HTTP".
                // Cada requisição deve se autenticar por conta própria (enviando o token)..sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configura a autorização para as requisições HTTP
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso público (permitAll) aos endpoints de login e registro
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                        // Permite acesso público à documentação do Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        // Exige a role "ADMIN" para qualquer requisição GET ou POST em "/api/**"
                        // O Spring automaticamente adiciona o prefixo "ROLE_" (ex: "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")

                        // Para qualquer outra requisição (anyRequest), exige que o usuário esteja autenticado
                        .anyRequest().authenticated()
                )

                // Adiciona nosso filtro personalizado (SecurityFilter) ANTES do filtro padrão do Spring (UsernamePasswordAuthenticationFilter)
                // Isso garante que nossa lógica de validação de token JWT rode primeiro.
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)

                // Constrói o objeto HttpSecurity
                .build();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // Obtém e expõe o Gerenciador de Autenticação do Spring
        // Ele será usado no nosso Controller de Autenticação para processar o login
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Cria um Bean para o BCryptPasswordEncoder
        // O Spring usará isso para criptografar as senhas ao registrar
        // e para comparar as senhas durante o login
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(corsOrigins));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}