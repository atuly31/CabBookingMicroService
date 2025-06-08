package com.cbs.User.dto;

import lombok.Data;
import org.apache.http.HttpStatus;


import java.time.LocalDateTime;

@Data
public class ApiResponseDto <T>{
     private String message;
     private int status;
     private LocalDateTime timestamp;
     private T data;

    public ApiResponseDto(String message,int status, LocalDateTime timestamp, T data) {
        this.message = message;
        this.timestamp = timestamp;
        this.data = data;
        this.status = status;
    }
}
