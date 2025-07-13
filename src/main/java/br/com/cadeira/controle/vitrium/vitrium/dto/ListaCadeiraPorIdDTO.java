package br.com.cadeira.controle.vitrium.vitrium.dto;

import br.com.cadeira.controle.vitrium.vitrium.entity.enums.ECadeira;

import java.time.OffsetDateTime;

public record ListaCadeiraPorIdDTO(
        String nomePaciente,
        String destino,
        int numeroClinica,
        OffsetDateTime dataEntrega,
        OffsetDateTime dataDevolucao,
        ECadeira cadeira,
        Boolean devolvida) {
}
