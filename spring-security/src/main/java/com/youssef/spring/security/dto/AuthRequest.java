package com.youssef.spring.security.dto;

import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank
        @Email
        String email,
        @NotBlank
        String password
) { }
