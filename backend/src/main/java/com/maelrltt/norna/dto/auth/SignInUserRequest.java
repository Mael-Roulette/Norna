package com.maelrltt.norna.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignInUserRequest(
        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email is not valid")
        String email,

        @NotBlank(message = "Password cannot be empty")
        String password
) {
}
