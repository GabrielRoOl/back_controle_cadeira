package br.com.cadeira.controle.vitrium.vitrium.exceptions;

public class ChairAlreadyReturned extends RuntimeException {

    public ChairAlreadyReturned() {
        super("Cadeira já foi devolvida");
    }
}
