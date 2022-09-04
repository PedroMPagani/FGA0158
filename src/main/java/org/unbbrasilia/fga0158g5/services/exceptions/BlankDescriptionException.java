package org.unbbrasilia.fga0158g5.services.exceptions;

public class BlankDescriptionException extends Exception {

    public BlankDescriptionException(String message){
        super(message);
    }

    @Override
    public void printStackTrace(){
        System.out.println(getMessage());
    }

}