package com.example.bankSystem.exceptions;

public class ClientNotFoundException extends Throwable {
    public ClientNotFoundException(String message) {
        super(message);
    }
}
