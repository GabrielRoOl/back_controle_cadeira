package br.com.cadeira.controle.vitrium.vitrium.dto;

import br.com.cadeira.controle.vitrium.vitrium.entity.enums.ECadeira;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record ListaCadeiraPorIdDTO(
        @NotBlank String nomePaciente,
        @NotBlank String destino,
        @NotNull int numeroClinica,
        OffsetDateTime dataEntrega,
        OffsetDateTime dataDevolucao,
        @NotNull ECadeira cadeira,
        Boolean devolvida) {
}
