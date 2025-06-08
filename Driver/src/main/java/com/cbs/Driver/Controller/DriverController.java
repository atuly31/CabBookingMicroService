package com.cbs.Driver.Controller;
import com.cbs.Driver.Entity.Driver;
import com.cbs.Driver.Service.IDriverService;
import com.cbs.Driver.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/driver")
public class DriverController {
    @Autowired
    IDriverService driverService;

    @PostMapping("/register")
    ResponseEntity<ApiResponseDto<DriverRegistrationDto>> addDriver(@RequestBody DriverRegistrationDto driverRegistrationDto){
        return new ResponseEntity<>(driverService.registerDriver(driverRegistrationDto), HttpStatus.CREATED);
    }

    @PostMapping ("/login")
    ResponseEntity<ApiResponseDto<DriverLoginDto>> driverLogin(@RequestBody DriverLoginDto driverLoginDto){
        return new ResponseEntity<>(driverService.driverLogin(driverLoginDto),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<DriverProfileDto> findDriverById(@PathVariable long id){
        return new ResponseEntity<>(driverService.getDriverById(id),HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    ResponseEntity<Driver> updateDriverStatus(@PathVariable Long id , @RequestParam Driver.DriverStatus status){
        Optional<Driver> updateDriver = driverService.updateDriverStatus(id,status);
        return updateDriver.map(driver -> new ResponseEntity<>(driver,HttpStatus.OK))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/rideHistory/{id}")
    ResponseEntity<List<RideDto>> getDriverRideHistory(@PathVariable long id){
        return new ResponseEntity<>(driverService.getDriverRideDetails(id),HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableDriverDto>> getAvailableDriver(){
        List<AvailableDriverDto> allAvailableDrivers = driverService.getAllAvailableDriver();
        if(allAvailableDrivers!=null){
            return new ResponseEntity<>(allAvailableDrivers ,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/complete-ride")
    public  ResponseEntity<String> updateRideStatus(@RequestParam Long id,@RequestParam Long rideId){
        return  new ResponseEntity<>(driverService.rideComplete(id,rideId),HttpStatus.OK);
    }




}
