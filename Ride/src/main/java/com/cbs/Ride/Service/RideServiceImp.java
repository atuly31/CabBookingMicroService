package com.cbs.Ride.Service;

import com.cbs.Ride.Client.DriverClient;
import com.cbs.Ride.Client.UserClient;
import com.cbs.Ride.Dto.AvailableDriverDto;
import com.cbs.Ride.Dto.DriverDto;
import com.cbs.Ride.Dto.RideDto;
import com.cbs.Ride.Dto.UserDto;
import com.cbs.Ride.Entity.Rides;
import com.cbs.Ride.Exception.UserDoesNotExistException;
import com.cbs.Ride.Repository.RideRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RideServiceImp implements IRideService{
    private static final Logger log = LoggerFactory.getLogger(RideServiceImp.class);
    @Autowired
    RideRepository rideRepository;
    @Autowired
    UserClient userClient;
    @Autowired
    DriverClient driverClient;
    @Autowired
    ModelMapper modelMapper;


    @Override
    public String requestRide(long userID, String pickupLocation, String dropoffLocation) {
        UserDto user = userClient.getUserById(userID);
        if(user==null) {
            throw new UserDoesNotExistException("User Does Not Exist");
        }
        Rides ride = new Rides(userID , pickupLocation,dropoffLocation, LocalDateTime.now());
        ride.setUserId(user.getId());
        ride.setActualFare(100);
        ride.setEndTime(LocalDateTime.now());
        ride.setStatus(Rides.RideStatus.SEARCHING_DRIVER);

        List<AvailableDriverDto> availableDrivers = driverClient.getAllAvailableDriver();
        if(availableDrivers.isEmpty()){
            log.error("No Driver Available for ride");
            return "No Driver Available for ride";
        }
        else{
            AvailableDriverDto assignedDriver = availableDrivers.getFirst();
            ride.setDriverId(assignedDriver.getId());
            ride.setStatus(Rides.RideStatus.DRIVER_ASSIGNED);
            driverClient.updateDriverStatus(assignedDriver.getId(), AvailableDriverDto.DriverStatus.ON_RIDE);
            rideRepository.save(ride);
            return  "Driver "+assignedDriver.getFirstName()+ " has been Assigned";
        }

    }


    @Override
    public List<RideDto> getUsersRides(long userId)throws UserDoesNotExistException {
        List<Rides> ridesDetails =  rideRepository.findByUserId(userId);
        if(ridesDetails.isEmpty()){
            throw new UserDoesNotExistException("User Does not Exist ");
        }
        return ridesDetails.stream().map(this::enrichRideDetails).toList();

    }

    public RideDto enrichRideDetails(Rides ride){
        RideDto rideDetails = modelMapper.map(ride, RideDto.class);
        if(ride.getUserId()!=null){
            try{
                UserDto user = userClient.getUserById(ride.getUserId());
                if(user==null){
                    throw new UserDoesNotExistException("User Does not Exist ");
                }
                rideDetails.setUserFirstName(user.getFirstName());
                rideDetails.setUserlastName(user.getLastName());
            } catch (UserDoesNotExistException e) {
                throw new UserDoesNotExistException("User Does not Exist");
            }
        }
        if(ride.getDriverId()!=null){
            try{
                DriverDto driver = driverClient.findDriverById(ride.getDriverId());

                System.out.println(driver.getFirstName()+ "  "+driver.getFirstName());
                rideDetails.setDriverFullname(driver.getFirstName(),driver.getLastName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return rideDetails;
    };

    @Override
    public String updateRideStatus(Long id){
        rideRepository.findById(id).map(rideDetails -> {
            rideDetails.setStatus(Rides.RideStatus.COMPLETED);
            return rideRepository.save(rideDetails);
        });
        return "Ride Status Changed Successfully";
    }

    @Override
    public List<RideDto> getDriverRidesDetails(long driverId) {
        List<Rides> ridesDetails =  rideRepository.findByDriverId(driverId);
        return ridesDetails.stream().map(this::enrichRideDetails).toList();

    }
}
