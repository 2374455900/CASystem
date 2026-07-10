package com.kuugae.ca;

import com.kuugae.ca.controller.AuthController;
import com.kuugae.ca.dto.ApiResponse;
import com.kuugae.ca.dto.LoginRequest;
import com.kuugae.ca.dto.RegisterRequest;
import com.kuugae.ca.dto.UserResponse;
import com.kuugae.ca.entity.User;
import com.kuugae.ca.repository.UserRepository;
import com.kuugae.ca.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setUsername("testuser");
        request.setPassword("Password123");

        ResponseEntity<ApiResponse<UserResponse>> response = authController.register(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("test@example.com", response.getBody().getData().getEmail());
    }

    @Test
    void testLogin() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("Password123");
        authController.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("Password123");

        ResponseEntity<ApiResponse<Map<String, Object>>> response = authController.login(loginRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData().get("token"));
    }
}
