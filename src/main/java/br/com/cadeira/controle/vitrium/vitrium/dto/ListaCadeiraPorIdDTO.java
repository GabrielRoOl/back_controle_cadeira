package br.com.cadeira.controle.vitrium.vitrium.dto;

import br.com.cadeira.controle.vitrium.vitrium.entity.enums.ECadeira;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record ListaCadeiraPorIdDTO(
        @NotBlank String nomePaciente,
        @NotBlank String destino,
        @NotNull int numeroClinica,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") OffsetDateTime dataEntrega,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") OffsetDateTime dataDevolucao,
        @NotNull ECadeira cadeira,
        Boolean devolvida) {
}
