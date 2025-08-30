package br.com.cadeira.controle.vitrium.authentication.dto;


import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @NotBlank(message = "Login não pode estar vazio")
        String login,
        @NotBlank(message = "A senha não pode estar vazia")
        String password
) {

}
