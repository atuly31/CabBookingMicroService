package com.cbs.User.Service;

import com.cbs.User.Entity.User;
import com.cbs.User.Exceptions.IncorrectPasswordException;

import com.cbs.User.Exceptions.UserAlreadyExist;
import com.cbs.User.Exceptions.UserDoesNotExistException;
import com.cbs.User.RideClient.RideClient;
import com.cbs.User.dto.*;
import lombok.val;
import org.apache.http.HttpStatus;
import org.modelmapper.ModelMapper;

import com.cbs.User.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImp.class);
    private final UserRepository userRepository;
    private final RideClient rideClient;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private  PasswordEncoder encoder;
    private final EmailServiceImp emailService;

    @Autowired
    UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder,RideClient rideClient,EmailServiceImp emailService,ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.rideClient = rideClient;
        this.modelMapper = modelMapper;

    }


    @Override
    public ApiResponseDto<UserRegistrationDto> registerUser(UserRegistrationDto userRegistrationDto) throws UserAlreadyExist {

     try{
         User user = modelMapper.map(userRegistrationDto, User.class);

         Map<String, Object> templateVariables = new HashMap<>();
         templateVariables.put("firstName", userRegistrationDto.getEmail());
         templateVariables.put("email", userRegistrationDto.getEmail());

         String subject = "Welcome to ApexRide!";

         user.setPasswordHash(passwordEncoder.encode(userRegistrationDto.getPasswordHash()));
         user.setLastProfileUpdate(LocalDateTime.now());
         user.setRegistrationDate(LocalDateTime.now());
         User Saveduser = userRepository.save(user);
         UserRegistrationDto userSavedDto = modelMapper.map(Saveduser, UserRegistrationDto.class);
         userSavedDto.setPasswordHash("");
         emailService.sendHtmlMail(userRegistrationDto.getEmail(), subject, "registration-welcome", templateVariables);

         return new ApiResponseDto<>("Registered Successfully", HttpStatus.SC_OK,LocalDateTime.now(),userSavedDto);

     } catch (DataIntegrityViolationException e) {
            if(e.getMostSpecificCause().getMessage().contains("gmail")){
                throw new UserAlreadyExist("Email Already Exist");
            } else if (e.getMostSpecificCause().getMessage().contains("Duplicate entry")) {
                throw new UserAlreadyExist("Phone number already exist");
            }
            else  {
                throw new UserAlreadyExist("Something went wrong");
            }

     }



    }

    @Override
    public ApiResponseDto<String> login(UserLoginDto userLoginDto) throws UserDoesNotExistException, IncorrectPasswordException {
       Optional<User> userInfo = userRepository.findByEmail(userLoginDto.getEmail());


        if (userInfo.isEmpty()) {
            throw new UserDoesNotExistException("User with mail id "+userLoginDto.getEmail()+" does not exit");
        }
        Optional<User> authiencatedUser =  userInfo.filter(user -> passwordEncoder.matches(userLoginDto.getPasswordHash(),user.getPasswordHash()));
        if (authiencatedUser.isPresent()) {

            return new ApiResponseDto<>("Login Successful",HttpStatus.SC_OK,LocalDateTime.now(),null);
        } else {
            throw new IncorrectPasswordException("Incorrect Password for user: " + userLoginDto.getEmail());
        }
    }


    @Override
    public ApiResponseDto<UserProfileDto> getUserProfile(Long id) throws UserDoesNotExistException {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new UserDoesNotExistException("User Does Not Exist");
        }
        UserProfileDto userDto;
        userDto = modelMapper.map(user.get(), UserProfileDto.class);
        return new ApiResponseDto<>("User Profile Successfully",HttpStatus.SC_OK,LocalDateTime.now(),userDto);
    }

    @Override
    public ApiResponseDto<List<RideDto>> getUsersRides(long id) {
        List<RideDto> rideDetails;
        rideDetails = rideClient.getUsersRides(id);
        return new ApiResponseDto<>("Success",HttpStatus.SC_OK,LocalDateTime.now(),rideDetails);
    }

    @Override
    public ApiResponseDto<RideDto> bookRide(long userID, String pickupLocation, String dropoffLocation) {
        ApiResponseDto<RideDto> rideDtoApiResponseDto;
        rideDtoApiResponseDto = rideClient.createRide(userID,pickupLocation,dropoffLocation);
        return rideDtoApiResponseDto;
    }
}
