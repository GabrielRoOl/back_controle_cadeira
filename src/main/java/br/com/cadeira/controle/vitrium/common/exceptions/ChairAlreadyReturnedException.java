package br.com.cadeira.controle.vitrium.common.exceptions;

public class ChairAlreadyReturnedException extends RuntimeException {

    public ChairAlreadyReturnedException() {
        super("Cadeira já foi devolvida");
    }
}
