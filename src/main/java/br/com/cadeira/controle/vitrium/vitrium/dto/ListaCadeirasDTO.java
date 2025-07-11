package br.com.cadeira.controle.vitrium.vitrium.dto;

import br.com.cadeira.controle.vitrium.vitrium.entity.enums.ECadeira;

import java.time.Instant;

public record ListaCadeirasDTO(
        String nomePaciente,
        String destino,
        int numeroClinica,
        Instant dataEntrega,
        Instant dataDevolucao,
        ECadeira cadeira,
        Boolean devolvida) {
}
