package br.com.cadeira.controle.vitrium.vitrium.dto;

import br.com.cadeira.controle.vitrium.vitrium.entity.Cadeiras;
import br.com.cadeira.controle.vitrium.vitrium.entity.enums.ECadeira;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;

public record DetalhamentoAdicionaCadeiraDTO(
        String nomePaciente,
        String destino,
        int numeroClinica,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") OffsetDateTime dataEntrega,
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
