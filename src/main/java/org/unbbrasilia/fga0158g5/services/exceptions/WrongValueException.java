package org.unbbrasilia.fga0158g5.services.exceptions;

public class WrongValueException extends Exception {

    public WrongValueException(String message){
        super(message);
    }

    @Override
    public void printStackTrace(){
        System.out.println(getMessage());
    }

}