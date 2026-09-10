package com.maelrltt.norna.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpUserRequest(
        @NotBlank(message = "Username cannot be empty")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 chars.")
        String username,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email is not valid")
        String email,

        @NotBlank(message = "Password cannot be empty")
        String password
) {}