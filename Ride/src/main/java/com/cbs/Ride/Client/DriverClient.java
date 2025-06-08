package com.cbs.Ride.Client;

import com.cbs.Ride.Dto.AvailableDriverDto;
import com.cbs.Ride.Dto.DriverDto;
import org.springframework.cloud.openfeign.FeignClient;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "DRIVER")
public interface DriverClient {
    @GetMapping("/api/v1/driver/{id}")
    DriverDto findDriverById(@PathVariable long id);

    @GetMapping("/api/v1/driver/available")
    List<AvailableDriverDto> getAllAvailableDriver();

    @PutMapping("/api/v1/driver/status/{id}")
    AvailableDriverDto updateDriverStatus(@PathVariable Long id, @RequestParam AvailableDriverDto.DriverStatus status);

}
