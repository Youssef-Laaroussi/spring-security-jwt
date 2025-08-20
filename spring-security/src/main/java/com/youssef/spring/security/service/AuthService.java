package com.youssef.spring.security.service;

import com.youssef.spring.security.dto.AuthRequest;
import com.youssef.spring.security.dto.AuthResponse;
import com.youssef.spring.security.dto.RegisterRequest;
import com.youssef.spring.security.exception.ApiException;
import com.youssef.spring.security.model.Role;
import com.youssef.spring.security.model.TokenType;
import com.youssef.spring.security.model.Tokn;
import com.youssef.spring.security.model.User;
import com.youssef.spring.security.repository.ToknRepository;
import com.youssef.spring.security.repository.UserRepository;
import com.youssef.spring.security.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ToknRepository toknRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;



    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ApiException("Email already in use", HttpStatus.BAD_REQUEST);
        }

        var user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        var savedUser = userRepository.save(user);
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(savedUser, accessToken);

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        return new AuthResponse(accessToken, refreshToken);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Tokn.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        toknRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = toknRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        toknRepository.saveAll(validUserTokens);
    }

























}
