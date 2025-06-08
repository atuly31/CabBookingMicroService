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
                HttpStatus.NO_CONTENT.value(),
                LocalDateTime.now()
        );
//        Map<String, Object> errorDetails = new HashMap<>();
//        errorDetails.put("timestamp", LocalDateTime.now());
//        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
//        errorDetails.put("error", "Not Found");
//        errorDetails.put("message", ex.getMessage());
//        errorDetails.put("path", request.getDescription(false).replace("uri=", "")); // Clean up path

        return new ResponseEntity<>(errorResponseMessage, HttpStatus.NOT_FOUND);
    }


}