package org.unbbrasilia.fga0158g5.services.exceptions;

public class RegistryUnallowedException extends Exception {

    public RegistryUnallowedException(String message){
        super(message);
    }

    @Override
    public void printStackTrace(){
        System.out.println(getMessage());
    }

}