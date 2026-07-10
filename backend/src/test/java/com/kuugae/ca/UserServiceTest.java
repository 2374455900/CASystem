package com.kuugae.ca;

import com.kuugae.ca.dto.LoginRequest;
import com.kuugae.ca.dto.RegisterRequest;
import com.kuugae.ca.entity.User;
import com.kuugae.ca.repository.UserRepository;
import com.kuugae.ca.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterUser() {
        String email = "test@example.com";
        String username = "testuser";
        String password = "Password123";

        User user = userService.register(email, username, password);

        assertNotNull(user);
        assertEquals(email, user.getEmail());
        assertEquals(username, user.getUsername());
        assertNotEquals(password, user.getPassword());
    }

    @Test
    void testRegisterDuplicateEmail() {
        String email = "test@example.com";
        String username1 = "user1";
        String username2 = "user2";
        String password = "Password123";

        userService.register(email, username1, password);

        assertThrows(Exception.class, () -> {
            userService.register(email, username2, password);
        });
    }

    @Test
    void testLoginSuccess() {
        String email = "test@example.com";
        String username = "testuser";
        String password = "Password123";

        userService.register(email, username, password);
        User loggedInUser = userService.login(email, password);

        assertNotNull(loggedInUser);
        assertEquals(email, loggedInUser.getEmail());
    }

    @Test
    void testLoginWrongPassword() {
        String email = "test@example.com";
        String username = "testuser";
        String password = "Password123";

        userService.register(email, username, password);

        assertThrows(Exception.class, () -> {
            userService.login(email, "WrongPassword");
        });
    }

    @Test
    void testLoginNonExistentUser() {
        assertThrows(Exception.class, () -> {
            userService.login("nonexistent@example.com", "anypassword");
        });
    }

    @Test
    void testVerifyPassword() {
        String email = "test@example.com";
        String username = "testuser";
        String password = "Password123";

        User user = userService.register(email, username, password);

        assertTrue(userService.verifyPassword(user, password));
        assertFalse(userService.verifyPassword(user, "WrongPassword"));
    }
}
