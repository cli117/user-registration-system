package com.torontoscrumteam.user_registration_system.controller;

import com.torontoscrumteam.user_registration_system.dto.LoginRequest;
import com.torontoscrumteam.user_registration_system.dto.request.SignupRequest;
import com.torontoscrumteam.user_registration_system.dto.response.LoginResponse;
import com.torontoscrumteam.user_registration_system.dto.response.SignupResponse;
import com.torontoscrumteam.user_registration_system.entity.User;
import com.torontoscrumteam.user_registration_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        SignupResponse response = userService.signup(signupRequest);
        if (response.isSuccess()) {
            System.out.println("signup successful");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Authentication successful, generate and return token
            User user = (User) authentication.getPrincipal();
            LoginResponse response = userService.generateLoginToken(user);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return new ResponseEntity<>(new LoginResponse(false, "Invalid username or password"), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/protected")
    public ResponseEntity<String> protectedResource() {
        return ResponseEntity.ok("This is a protected resource!");
    }
}