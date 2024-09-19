package com.example.bankSystem.exceptions;

public class NegativeBalanceException extends Throwable{
    public NegativeBalanceException(String message) {
        super(message);
    }
}
