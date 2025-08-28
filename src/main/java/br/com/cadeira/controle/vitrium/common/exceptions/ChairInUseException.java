package br.com.cadeira.controle.vitrium.common.exceptions;

public class ChairInUseException extends RuntimeException {

    public ChairInUseException() {
        super("Cadeira em uso");
    }
}
