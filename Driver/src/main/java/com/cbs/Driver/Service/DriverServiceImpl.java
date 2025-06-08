package com.cbs.Driver.Service;

import com.cbs.Driver.Client.RideClient;
import com.cbs.Driver.Entity.Driver;
import com.cbs.Driver.Exception.DriverAlreadyExist;
import com.cbs.Driver.Exception.DriverDoesNotExistException;
import com.cbs.Driver.Exception.IncorrectPasswordException;
import com.cbs.Driver.Repository.IDriverRepository;
import com.cbs.Driver.dto.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DriverServiceImpl implements IDriverService{
    private static final Logger log = LoggerFactory.getLogger(DriverServiceImpl.class);
    @Autowired
    IDriverRepository driverRepository;  // Instance of JPA Repository to Perform database Operations
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    RideClient rideClient;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public ApiResponseDto<DriverRegistrationDto> registerDriver(DriverRegistrationDto driverRegistrationDto) {

        Driver mappedDriver = modelMapper.map(driverRegistrationDto, Driver.class);//Maps the driverRegistrationDto to Driver Entity
        mappedDriver.setRegistrationDate(LocalDateTime.now());
        mappedDriver.setPasswordHash(passwordEncoder.encode(driverRegistrationDto.getPasswordHash()));
        try{
            Driver savedDriver = driverRepository.save(mappedDriver); //saves the driver details to DB
            DriverRegistrationDto savedDriverDto = modelMapper.map(savedDriver, DriverRegistrationDto.class);
            savedDriverDto.setPasswordHash("");
            return new ApiResponseDto<>(true,"Registered Successfully",savedDriverDto); //return custom response

        } catch (DataIntegrityViolationException ex) {
            throw new DriverDoesNotExistException("Driver Does not Exist");
        }



    }

    @Override
    public ApiResponseDto<DriverLoginDto> driverLogin(DriverLoginDto driverLoginDto) {
        Optional<Driver> driver = driverRepository.findByEmail(driverLoginDto.getEmail());
        if(driver.isEmpty()){
            throw  new DriverDoesNotExistException("Driver does Not Exist");
        }
        Optional<Driver> authenticatedDriver = driver.filter(driverObject -> passwordEncoder.matches(driverLoginDto.getPasswordHash(),driverObject.getPasswordHash()));

        if(authenticatedDriver.isEmpty()){
            throw new IncorrectPasswordException("Incorrect password for driver with email " + driverLoginDto.getEmail());
        }
        else {
            driver.get().setStatus((Driver.DriverStatus.AVAILABLE));
            driverRepository.save(driver.get());
            driverLoginDto.setId(driver.get().getId());
            driverLoginDto.setPasswordHash("");
            return new ApiResponseDto<>(true,"Driver Successfully Logged In!",driverLoginDto);

        }
    }

    @Override
    public ApiResponseDto<DriverProfileDto> getDriverById(Long id) {
        Optional<Driver> driver = driverRepository.findById(id);
        if(driver.isEmpty()){
            throw  new DriverDoesNotExistException("Driver does Not Exist");
        }
        DriverProfileDto driverProfile = modelMapper.map(driver.get(), DriverProfileDto.class);  //map the driver Entity to driverProfileDTO
        return new ApiResponseDto<>(true,"Driver Successfully Logged In",driverProfile);
    }



    @Override
    public Optional<Driver> updateDriverStatus(Long id, Driver.DriverStatus status) {
      return driverRepository.findById(id).map(driver1 -> {
             driver1.setStatus(status); //updates driver Status to DB
             return driverRepository.save(driver1);  //saves the updated driver Status to DB
        });
    }

    @Override
    public List<AvailableDriverDto> getAllAvailableDriver() {
        List<Driver> driverList = driverRepository.findByStatus(Driver.DriverStatus.AVAILABLE);
        if(driverList.isEmpty()) {
            log.error("No Driver is Available for ride ");
        }
        System.out.println(driverList);
        return driverList.stream().map(driver -> modelMapper.map(driver,AvailableDriverDto.class)).collect(Collectors.toList());

    }

    @Override
    public String rideComplete(Long id,Long rideID) {
        driverRepository.findById(id).map(driver -> {
            driver.setStatus(Driver.DriverStatus.AVAILABLE);
            return driverRepository.save(driver);

        });
        rideClient.updateRideStatus(rideID);

        return "Ride has been Completed";
    }

    @Override
    public List<RideDto> getDriverRideDetails(long driverId) {
        List<RideDto> rideDto = rideClient.getDriverRides(driverId);
        return rideDto;
    }
}
