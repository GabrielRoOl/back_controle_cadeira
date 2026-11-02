package br.com.cadeira.controle.vitrium.authentication.controller;

import br.com.cadeira.controle.vitrium.authentication.dto.AuthenticationDTO;
import br.com.cadeira.controle.vitrium.authentication.dto.RegisterDTO;
import br.com.cadeira.controle.vitrium.authentication.dto.TokenResponseDTO;
import br.com.cadeira.controle.vitrium.authentication.entity.User;
import br.com.cadeira.controle.vitrium.authentication.entity.enums.UserRole;
import br.com.cadeira.controle.vitrium.authentication.repository.UserRepository;
import br.com.cadeira.controle.vitrium.common.configs.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Operation(security = {}) // Annotation que indica a Swagger que esse é um endpoint publico
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO dto) {
        if (this.userRepository.findByLogin(dto.login()) == null) {
            throw new RuntimeException("Usuario ou senha inválido");
        }

        var password = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
        var auth = this.authenticationManager.authenticate(password);

        var user = (User) auth.getPrincipal();
        var token = tokenService.generateToken(user);

        return ResponseEntity.ok(new TokenResponseDTO(token));
    }

    @Operation(security = {}) // Annotation que indica a Swagger que esse é um endpoint publico
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO dto) {
        if (!Objects.equals(dto.password(), dto.confirmPassword())) {
            throw new RuntimeException("Senhas não são iguais");
        }
        if (this.userRepository.findByLogin(dto.login()) != null) {
            throw new UsernameNotFoundException("usuario invalido");
        }
        // O cadastro é público, mas só um ADMIN autenticado pode criar outro ADMIN
        if (dto.role() == UserRole.ADMIN && !usuarioAtualEhAdmin()) {
            throw new AccessDeniedException("Apenas um ADMIN pode cadastrar outro ADMIN");
        }

        String encryptedPassowrd = new BCryptPasswordEncoder().encode(dto.password());

        User user = new User(dto.login(), encryptedPassowrd, dto.role());

        this.userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    private boolean usuarioAtualEhAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
