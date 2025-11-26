package br.com.cadeira.controle.vitrium.common.exceptions;

public class ChairNotFoundException extends RuntimeException {

    public ChairNotFoundException() {
        super("Cadeira não encontrada.");
    }
}
