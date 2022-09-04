package org.unbbrasilia.fga0158g5.services.exceptions;

public class InvalidAccessValueException extends Exception {

    public InvalidAccessValueException(String message){
        super(message);
    }

    @Override
    public void printStackTrace(){
        System.out.println(getMessage());
    }

}