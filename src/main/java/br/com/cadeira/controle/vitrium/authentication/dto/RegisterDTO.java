package br.com.cadeira.controle.vitrium.authentication.dto;

import br.com.cadeira.controle.vitrium.authentication.entity.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
        @NotBlank(message = "Nome não pode ser vazio")
        String login,
        @NotBlank(message = "A senha não pode estar vazia")
        String password,
        String confirmPassword,
        @NotNull(message = "A role não pode estar vazia.\nValores válidos 'ADMIN' e 'USER'")
        UserRole role
) {
}
