package com.ecommerce.app.exceptions;

public class InvalidCredentialException extends  RuntimeException{
    public InvalidCredentialException(String message){
        super(message);
    }
}
