package com.cbs.User.Service;

import com.cbs.User.Entity.User;
import com.cbs.User.Exceptions.IncorrectPasswordException;

import com.cbs.User.Exceptions.UserAlreadyExist;
import com.cbs.User.Exceptions.UserDoesNotExistException;
import com.cbs.User.RideClient.RideClient;
import com.cbs.User.dto.RideDto;
import com.cbs.User.dto.UserLoginDto;
import com.cbs.User.dto.UserProfileDto;
import org.modelmapper.ModelMapper;

import com.cbs.User.Repository.UserRepository;
import com.cbs.User.dto.UserRegistrationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    RideClient rideClient;

//    @Autowired
//    EmailServiceImp emailService;
     @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public UserRegistrationDto registerUser(UserRegistrationDto userRegistrationDto) throws UserAlreadyExist {

        if(userRepository.existsByEmail(userRegistrationDto.getEmail())) {
            throw new UserAlreadyExist("User with email '" + userRegistrationDto.getEmail() + "' already exists.");
        }

        if(userRepository.existsByPhoneNumber(userRegistrationDto.getPhoneNumber())) {
            throw new UserAlreadyExist("This Phone Number Already exist. Please try Another!!!");
        }

        User user = modelMapper.map(userRegistrationDto, User.class);

        // Prepare variables for the template
//        Map<String, Object> templateVariables = new HashMap<>();
//        templateVariables.put("firstName", userRegistrationDto.getEmail());
//        templateVariables.put("email", userRegistrationDto.getEmail());

        // Use the new HTML email method
//        String subject = "Welcome to ApexRide!";
        // Subject can still be static or from properties
        user.setPasswordHash(passwordEncoder.encode(userRegistrationDto.getPasswordHash()));
        user.setLastProfileUpdate(LocalDateTime.now());
        user.setRegistrationDate(LocalDateTime.now());
        User Saveduser = userRepository.save(user);
        UserRegistrationDto userSavedDto = modelMapper.map(Saveduser, UserRegistrationDto.class);
//        emailService.sendHtmlMail(userRegistrationDto.getEmail(), subject, "registration-welcome", templateVariables);

        return userSavedDto;


    }

    @Override
    public String login(UserLoginDto userLoginDto) throws UserDoesNotExistException, IncorrectPasswordException {
       Optional<User> userInfo = userRepository.findByEmail(userLoginDto.getEmail());


        if (userInfo.isEmpty()) {
            throw new UserDoesNotExistException("User with mail id "+userLoginDto.getEmail()+" does not exit");
        }
        Optional<User> authiencatedUser =  userInfo.filter(user -> passwordEncoder.matches(userLoginDto.getPasswordHash(),user.getPasswordHash()));
        if (!authiencatedUser.isEmpty()) {

            return "User Logged In Successfully";
        } else {
            throw new IncorrectPasswordException("Incorrect Password for user: " + userLoginDto.getEmail());
        }
    }


    @Override
    public UserProfileDto getUserProfile(Long id) throws UserDoesNotExistException {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new UserDoesNotExistException("User Does Not Exist");
        }
        UserProfileDto userDto = modelMapper.map(user.get(), UserProfileDto.class);
        return userDto;
    }

    @Override
    public List<RideDto> getUsersRides(long id) {
        List<RideDto> rideDetails = rideClient.getUsersRides(id);
        return rideDetails;
    }
}
