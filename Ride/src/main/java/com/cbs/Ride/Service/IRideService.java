package com.cbs.Ride.Service;

import com.cbs.Ride.Dto.RideDto;
import com.cbs.Ride.Entity.Rides;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IRideService {
    String requestRide(long userID, String pickupLocation, String dropoffLocation);
    List<RideDto> getUsersRides(long userId);
    String updateRideStatus(Long id);
    List<RideDto> getDriverRidesDetails(long driverId);
}
