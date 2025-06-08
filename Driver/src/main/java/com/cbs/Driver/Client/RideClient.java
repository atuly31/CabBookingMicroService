package com.cbs.Driver.Client;

import com.cbs.Driver.dto.RideDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="RIDE")
public interface RideClient {
    @PutMapping("/api/v1/rides/update-rideStatus")
    public String updateRideStatus(@RequestParam Long id);

    @GetMapping("/api/v1/rides/drivers/{id}")
    List<RideDto> getDriverRides(@PathVariable("id") long id);
}
