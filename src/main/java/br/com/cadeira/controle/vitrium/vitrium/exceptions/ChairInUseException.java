package br.com.cadeira.controle.vitrium.vitrium.exceptions;

public class ChairInUseException extends RuntimeException {

    public ChairInUseException() {
        super("Cadeira em uso");
    }
}
