package org.unbbrasilia.fga0158g5.services.exceptions;

public class RegisterNotFoundException extends Exception {

    public RegisterNotFoundException(String message){
        super(message);
    }

    @Override
    public void printStackTrace(){
        System.out.println(getMessage());
    }

}