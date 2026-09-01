package com.maelrltt.norna.mapper;

import com.maelrltt.norna.dto.UpdateUserRequest;
import com.maelrltt.norna.dto.UserRequest;
import com.maelrltt.norna.dto.UserResponse;
import com.maelrltt.norna.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequest request) {
        return User.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .build();
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public void updateEntityFromRequest(User user, UpdateUserRequest request) {
        if (request.username() != null && !request.username().isBlank()) {
            user.setUsername(request.username());
        }
        if (request.email() != null && !request.email().isBlank()) {
            user.setEmail(request.email());
        }
    }
}