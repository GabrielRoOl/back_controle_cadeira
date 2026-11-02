package br.com.cadeira.controle.vitrium.vitrium.dto;

import br.com.cadeira.controle.vitrium.vitrium.entity.enums.ECadeira;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;

public record ListaCadeirasDTO(
        Long id,
        String nomePaciente,
        String destino,
        int numeroClinica,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "America/Sao_Paulo") OffsetDateTime dataEntrega,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "America/Sao_Paulo") OffsetDateTime dataDevolucao,
        ECadeira cadeira,
        Boolean devolvida) {
}
