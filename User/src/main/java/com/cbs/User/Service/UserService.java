package com.cbs.User.Service;


import com.cbs.User.Exceptions.IncorrectPasswordException;
import com.cbs.User.Exceptions.UserDoesNotExistException;
import com.cbs.User.dto.RideDto;
import com.cbs.User.dto.UserLoginDto;
import com.cbs.User.dto.UserProfileDto;
import com.cbs.User.dto.UserRegistrationDto;

import java.util.List;


public interface UserService {
      UserRegistrationDto registerUser(UserRegistrationDto userRegistrationDto);
      String login (UserLoginDto userLoginDto) throws UserDoesNotExistException, IncorrectPasswordException;
      UserProfileDto getUserProfile(Long id);
      List<RideDto> getUsersRides(long id);
}
