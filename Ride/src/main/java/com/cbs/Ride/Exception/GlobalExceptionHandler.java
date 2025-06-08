//package com.cbs.Ride.Exception; // Or a more appropriate package for your exception handlers
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(UserDoesNotExistException.class)
//    public ResponseEntity<Map<String, Object>> handleUserDoesNotExistException(
//            UserDoesNotExistException ex, WebRequest request) {
//
//        Map<String, Object> errorDetails = new HashMap<>();
//        errorDetails.put("timestamp", LocalDateTime.now());
//        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
//        errorDetails.put("error", "Not Found");
//        errorDetails.put("message", ex.getMessage());
//        errorDetails.put("path", request.getDescription(false).replace("uri=", "")); // Clean up path
//
//        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
//    }
//
//
//}