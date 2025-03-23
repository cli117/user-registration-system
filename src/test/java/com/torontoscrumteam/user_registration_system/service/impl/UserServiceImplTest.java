package com.torontoscrumteam.user_registration_system.service.impl;

import com.torontoscrumteam.user_registration_system.dto.request.SignupRequest;
import com.torontoscrumteam.user_registration_system.dto.response.SignupResponse;
import com.torontoscrumteam.user_registration_system.entity.User;
import com.torontoscrumteam.user_registration_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private RedisTemplate<String, String> redisTemplate; // 修改了类型

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private ValueOperations<String, String> valueOperations; // 修改了类型

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void signup_SuccessfulRegistration() {
        SignupRequest signupRequest = new SignupRequest("testuser", "password", "test@example.com");
        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("hashedPassword");

        User savedUser = new User("testuser", "hashedPassword", "test@example.com");
        savedUser.setId(1L);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        SignupResponse response = userService.signup(signupRequest);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Registration successful");
        assertThat(savedUser.getId()).isGreaterThan(0);
        verify(userRepository, times(1)).save(any(User.class));
        verify(redisTemplate.opsForValue(), times(1)).set(eq("user:testuser"), eq(String.valueOf(savedUser.getId())), anyLong(), any(TimeUnit.class)); // 修改了验证
    }

    @Test
    void signup_UsernameAlreadyExists() {
        SignupRequest signupRequest = new SignupRequest("existinguser", "password", "test@example.com");
        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(true);

        SignupResponse response = userService.signup(signupRequest);

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("Username already exists");
        verify(userRepository, never()).save(any(User.class));
        verify(redisTemplate.opsForValue(), never()).set(anyString(), any(), anyLong(), any());
    }

    @Test
    void signup_EmailAlreadyExists() {
        SignupRequest signupRequest = new SignupRequest("testuser", "password", "existing@example.com");
        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        SignupResponse response = userService.signup(signupRequest);

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("Email already exists");
        verify(userRepository, never()).save(any(User.class));
        verify(redisTemplate.opsForValue(), never()).set(anyString(), any(), anyLong(), any());
    }
}