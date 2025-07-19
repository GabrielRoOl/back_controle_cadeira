package br.com.cadeira.controle.vitrium.vitrium.exceptions;

public class ChairNotFoundException extends RuntimeException {

    public ChairNotFoundException() {
        super("Cadeira não encontrada.");
    }
}
