package com.kuugae.ca.service;

import com.kuugae.ca.dto.UserResponse;
import com.kuugae.ca.entity.User;
import com.kuugae.ca.exception.BusinessException;
import com.kuugae.ca.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(401, "邮箱或密码错误"));

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new BusinessException(401, "邮箱或密码错误");
        }

        return user;
    }

    @Transactional
    public User register(String email, String username, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(400, "邮箱已被注册");
        }

        if (userRepository.existsByUsername(username)) {
            throw new BusinessException(400, "用户名已被使用");
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setAuthority("0");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setDeletedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public boolean verifyPassword(User user, String password) {
        return BCrypt.checkpw(password, user.getPassword());
    }

    public UserResponse convertToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .authority(user.getAuthority())
                .createdAt(user.getCreatedAt() != null ?
                        user.getCreatedAt().format(FORMATTER) : null)
                .build();
    }

    public List<UserResponse> convertToResponseList(List<User> users) {
        return users.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
}
