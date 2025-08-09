package br.com.cadeira.controle.vitrium.vitrium.exceptions;

public class ChairDoesNotExistException extends RuntimeException {

    public ChairDoesNotExistException() {
        super("Cadeira não existe");
    }
}
