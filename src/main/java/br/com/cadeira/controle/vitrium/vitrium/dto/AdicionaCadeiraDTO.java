package br.com.cadeira.controle.vitrium.vitrium.dto;

import br.com.cadeira.controle.vitrium.vitrium.entity.enums.ECadeira;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdicionaCadeiraDTO(
        @NotBlank String nomePaciente,
        @NotBlank String destino,
        @NotNull int numeroClinica,
        ECadeira cadeira
) {
}
