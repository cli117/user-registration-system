package com.torontoscrumteam.user_registration_system.service.impl;

import com.torontoscrumteam.user_registration_system.dto.request.SignupRequest;
import com.torontoscrumteam.user_registration_system.dto.response.LoginResponse;
import com.torontoscrumteam.user_registration_system.dto.response.SignupResponse;
import com.torontoscrumteam.user_registration_system.entity.User;
import com.torontoscrumteam.user_registration_system.repository.UserRepository;
import com.torontoscrumteam.user_registration_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public SignupResponse signup(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return new SignupResponse(false, "Username already exists");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return new SignupResponse(false, "Email already exists");
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setRegistrationDate(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // Cache the user
        String cacheKey = "user:" + savedUser.getUsername();
        redisTemplate.opsForValue().set(cacheKey, String.valueOf(savedUser.getId()), 1, TimeUnit.HOURS); // Cache user ID with username as key for 1 hour

        return new SignupResponse(true, "Registration successful");
    }

    @Override
    public LoginResponse login(String usernameOrEmail, String password) {
//        Optional<User> userOptional = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
//
//        if (userOptional.isEmpty()) {
//            return new LoginResponse(false, "Invalid username or email");
//        }
//
//        User user = userOptional.get();
//        if (passwordEncoder.matches(password, user.getPassword())) {
//            String token = generateToken();
//            String redisKey = "user:token:" + token;
//            redisTemplate.opsForValue().set(redisKey, String.valueOf(user.getId()), 1, TimeUnit.HOURS);
//            return new LoginResponse(true, "Login successful", token);
//        } else {
//            return new LoginResponse(false, "Invalid password");
//        }

        // Spring Security is now handling the password cheking
        return null;

    }

    @Override
    public LoginResponse generateLoginToken(User user) {
        String token = generateToken();
        String redisKey = "user:token:" + token;
        redisTemplate.opsForValue().set(redisKey, String.valueOf(user.getId()), 1, TimeUnit.HOURS);
        return new LoginResponse(true, "Login successful", token);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}