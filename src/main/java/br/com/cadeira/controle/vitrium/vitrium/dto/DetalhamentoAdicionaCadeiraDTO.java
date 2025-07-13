package br.com.cadeira.controle.vitrium.vitrium.dto;

import br.com.cadeira.controle.vitrium.vitrium.entity.Cadeiras;
import br.com.cadeira.controle.vitrium.vitrium.entity.enums.ECadeira;

import java.time.OffsetDateTime;

public record DetalhamentoAdicionaCadeiraDTO(
        String nomePaciente,
        String destino,
        int numeroClinica,
        OffsetDateTime dataEntrega,
        ECadeira cadeira,
        Boolean devolvida) {

    public DetalhamentoAdicionaCadeiraDTO(Cadeiras novaCadeira) {
        this(novaCadeira.getNomePaciente(),
                novaCadeira.getDestino()
                , novaCadeira.getNmrClinica(),
                novaCadeira.getDtEntrega(),
                novaCadeira.getCadeira(),
                novaCadeira.getDevolvida());
    }
}
