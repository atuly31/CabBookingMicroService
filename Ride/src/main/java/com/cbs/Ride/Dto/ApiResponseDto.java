package com.cbs.Ride.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponseDto<T>{
     private String message;
     private LocalDateTime timestamp;
     private T data;

    public ApiResponseDto(String message, LocalDateTime timestamp, T data) {
        this.message = message;
        this.timestamp = timestamp;
        this.data = data;
    }
}
