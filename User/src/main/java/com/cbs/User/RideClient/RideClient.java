package com.cbs.User.RideClient;

import com.cbs.User.dto.RideDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="RIDE")
public interface RideClient {
    @GetMapping("/api/v1/rides/users/{id}")
    List <RideDto> getUsersRides(@PathVariable("id") long id);
}
