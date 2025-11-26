package br.com.cadeira.controle.vitrium.common.exceptions;

public class ChairDoesNotExistException extends RuntimeException {

    public ChairDoesNotExistException() {
        super("Cadeira não existe");
    }
}
