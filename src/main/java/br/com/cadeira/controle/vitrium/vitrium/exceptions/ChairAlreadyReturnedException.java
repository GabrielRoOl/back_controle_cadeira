package br.com.cadeira.controle.vitrium.vitrium.exceptions;

public class ChairAlreadyReturnedException extends RuntimeException {

    public ChairAlreadyReturnedException() {
        super("Cadeira já foi devolvida");
    }
}
