package br.com.cadeira.controle.vitrium.vitrium.dto;

import br.com.cadeira.controle.vitrium.vitrium.entity.enums.ECadeira;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdicionaCadeiraDTO(
        @NotBlank String nomePaciente,
        @NotBlank String destino,
        @NotNull int numeroClinica,
        @NotNull(message = "Tipo invalido, valores permitidos: CADEIRA_01, CADEIRA_02, CADEIRA_03, CADEIRA_04, CADEIRA_05, CADEIRA_06, CADEIRA_07 e CADEIRA_08") ECadeira cadeira
) {
}
