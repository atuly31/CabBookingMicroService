package com.cbs.Ride.Controller;

import com.cbs.Ride.Dto.ApiResponseDto;
import com.cbs.Ride.Dto.RideDto;
import com.cbs.Ride.Entity.Rides;
import com.cbs.Ride.Exception.UserDoesNotExistException;
import com.cbs.Ride.Service.IRideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rides")
public class RideController {
    @Autowired
    IRideService rideService;

    @PostMapping("/book-ride/{userID}")
    public ResponseEntity<ApiResponseDto<RideDto>> createRide (@PathVariable long userID, @RequestParam String pickupLocation, @RequestParam String dropoffLocation){
        return new ResponseEntity<>(rideService.requestRide(userID, pickupLocation,dropoffLocation), HttpStatus.OK);
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<List<RideDto>> getUsersRides(@PathVariable long id) throws UserDoesNotExistException {
        return new ResponseEntity<List<RideDto>>(rideService.getUsersRides(id),HttpStatus.OK);
    }

    @GetMapping("/drivers/{id}")
    ResponseEntity<List<RideDto>> getDriverRides(@PathVariable long id){
        return new ResponseEntity<List<RideDto>>(rideService.getDriverRidesDetails(id),HttpStatus.OK);
    }

    @PutMapping("/update-rideStatus")
    public ResponseEntity<String> updateRideStatus(@RequestParam Long id){
        if(Boolean.parseBoolean(rideService.updateRideStatus(id))){
            return new ResponseEntity<>("Ride Status Changed",HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


}
