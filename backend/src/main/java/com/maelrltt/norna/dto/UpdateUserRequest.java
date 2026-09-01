package com.maelrltt.norna.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 chars.")
        String username,

        @Email(message = "Email is not valid")
        String email
) {}