package com.cbs.User.RideClient;

import com.cbs.User.dto.ApiResponseDto;
import com.cbs.User.dto.RideDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="RIDE")
public interface RideClient {
    @GetMapping("/api/v1/rides/users/{id}")
    List <RideDto> getUsersRides(@PathVariable("id") long id);

    @PostMapping("/api/v1/rides/book-ride/{userID}")
    ApiResponseDto<RideDto> createRide(@PathVariable long userID, @RequestParam String pickupLocation, @RequestParam String dropoffLocation);
}
