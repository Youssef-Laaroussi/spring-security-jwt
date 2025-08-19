package com.youssef.spring.security.dto;

public record UserDto(
    Long id,
    String email,
    String firstName,
    String lastName,
    String role
){}