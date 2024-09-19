package com.example.bankSystem.exceptionHandlers;

import com.example.bankSystem.exceptions.AccountNotFoundException;
import com.example.bankSystem.exceptions.ClientNotFoundException;
import com.example.bankSystem.exceptions.NegativeBalanceException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class MainExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ClientNotFoundException.class, AccountNotFoundException.class, NegativeBalanceException.class} )
    public ErrorInfo handleClientException(HttpServletRequest req, Exception ex){
        return new ErrorInfo(req.getRequestURL().toString(), ex);
    }
}
