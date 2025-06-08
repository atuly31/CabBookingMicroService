package com.cbs.User.Controller;

import com.cbs.User.Exceptions.IncorrectPasswordException;
import com.cbs.User.Exceptions.UserDoesNotExistException;
import com.cbs.User.Service.UserService;
import com.cbs.User.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping ("/register")
    ResponseEntity<ApiResponseDto<UserRegistrationDto>> addUser(@RequestBody UserRegistrationDto userRegistrationDto){
        return new ResponseEntity<>(userService.registerUser(userRegistrationDto), HttpStatus.CREATED);
    }
    @PostMapping("/login")
    ResponseEntity<ApiResponseDto<String>> login(@RequestBody UserLoginDto userLoginDto) throws UserDoesNotExistException, IncorrectPasswordException {

        return new ResponseEntity<>(userService.login(userLoginDto),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    ResponseEntity<ApiResponseDto<UserProfileDto>> getUserById(@PathVariable Long id) throws UserDoesNotExistException{
        return new ResponseEntity<>(userService.getUserProfile(id),HttpStatus.OK);
    }

    @GetMapping("/rides/{id}")
    public ResponseEntity<ApiResponseDto<List<RideDto>>> getUserRides(@PathVariable  long id){
        return  new ResponseEntity<>(userService.getUsersRides(id),HttpStatus.OK);
    }

    @PostMapping("/book-ride/{userID}")
    public ResponseEntity<ApiResponseDto<RideDto>> getUserBookRide(@PathVariable long userID, @RequestParam String pickupLocation, @RequestParam String dropoffLocation){
        return new ResponseEntity<>(userService.bookRide(userID,pickupLocation,dropoffLocation),HttpStatus.OK);
    }

}
