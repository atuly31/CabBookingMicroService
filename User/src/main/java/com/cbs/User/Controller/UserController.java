package com.cbs.User.Controller;

import com.cbs.User.Exceptions.IncorrectPasswordException;
import com.cbs.User.Exceptions.UserDoesNotExistException;
import com.cbs.User.Service.UserService;
import com.cbs.User.dto.RideDto;
import com.cbs.User.dto.UserLoginDto;
import com.cbs.User.dto.UserProfileDto;
import com.cbs.User.dto.UserRegistrationDto;
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
    ResponseEntity<UserRegistrationDto> addUser(@RequestBody UserRegistrationDto userRegistrationDto){
        return new ResponseEntity<UserRegistrationDto>(userService.registerUser(userRegistrationDto), HttpStatus.CREATED);
    }
    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto) throws UserDoesNotExistException, IncorrectPasswordException {

        return new ResponseEntity<String>(userService.login(userLoginDto),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    ResponseEntity<UserProfileDto> getUserById(@PathVariable Long id) throws UserDoesNotExistException{
        return new ResponseEntity<UserProfileDto>(userService.getUserProfile(id),HttpStatus.OK);
    }

    @GetMapping("/rides/{id}")
    public ResponseEntity<List<RideDto>> getUserRides(@PathVariable  long id){
        return  new ResponseEntity<List<RideDto>>(userService.getUsersRides(id),HttpStatus.OK);
    }

}
