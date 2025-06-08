package com.cbs.User.Exceptions;//package com.cbs.User.Exceptions;
 // Or a more appropriate package for your exception handlers

import com.cbs.User.Exceptions.UserDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<ErrorResponseMessage> handleUserDoesNotExistException(
            UserDoesNotExistException ex, WebRequest request) {


        ErrorResponseMessage errorResponseMessage = new ErrorResponseMessage(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseMessage, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UserAlreadyExist.class)
    public ResponseEntity<ErrorResponseMessage> handleUserAlreadyException(UserAlreadyExist ex, WebRequest request) {
        ErrorResponseMessage errorResponseMessage = new ErrorResponseMessage(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseMessage, HttpStatus.NOT_FOUND);
    }
   @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ErrorResponseMessage> handlePasswordIncorrectException(IncorrectPasswordException ex, WebRequest request) {
        ErrorResponseMessage errorResponseMessage = new ErrorResponseMessage(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseMessage, HttpStatus.UNAUTHORIZED);
    }


}