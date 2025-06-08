package com.cbs.Driver.Service;

import com.cbs.Driver.Entity.Driver;
import com.cbs.Driver.dto.*;

import java.util.List;
import java.util.Optional;

public interface IDriverService {

    ApiResponseDto<DriverRegistrationDto> registerDriver(DriverRegistrationDto driverRegistrationDto);
    ApiResponseDto<DriverProfileDto> getDriverById (Long id);
    ApiResponseDto<DriverLoginDto>  driverLogin(DriverLoginDto driverLoginDto);
    Optional<Driver> updateDriverStatus(Long id , Driver.DriverStatus status);
    List<AvailableDriverDto> getAllAvailableDriver();
    String rideComplete(Long id, Long rideId);
    List<RideDto> getDriverRideDetails(long driverId);
}
