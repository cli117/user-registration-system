package com.torontoscrumteam.user_registration_system.service;

import com.torontoscrumteam.user_registration_system.dto.request.SignupRequest;
import com.torontoscrumteam.user_registration_system.dto.response.LoginResponse;
import com.torontoscrumteam.user_registration_system.dto.response.SignupResponse;
import com.torontoscrumteam.user_registration_system.entity.User;

public interface UserService {

    SignupResponse signup(SignupRequest signupRequest);

    LoginResponse login(String usernameOrEmail, String password);

    LoginResponse generateLoginToken(User user); // New method
}