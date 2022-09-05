package org.unbbrasilia.fga0158g5.services.exceptions;

public class InvalidMenuOptionException extends Exception {

    public InvalidMenuOptionException(){
        super("Opção de menu inválida, voltando ao menu principal.");
    }

    public InvalidMenuOptionException(String message){
        super(message);
    }

    @Override
    public void printStackTrace(){
        System.out.println(getMessage());
    }

}