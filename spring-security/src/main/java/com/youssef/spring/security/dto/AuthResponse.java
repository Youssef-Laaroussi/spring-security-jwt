package com.youssef.spring.security.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) { }
