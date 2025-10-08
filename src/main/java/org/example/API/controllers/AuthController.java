package org.example.API.controllers;

import com.google.gson.Gson;
import org.example.Domain.dtos.RequestDto;
import org.example.Domain.dtos.ResponseDto;
import org.example.Domain.dtos.auth.LoginRequestDto;
import org.example.Domain.dtos.auth.RegisterRequestDto;
import org.example.Domain.dtos.auth.UserResponseDto;
import org.example.Domain.models.User;
import org.example.DataAccess.services.AuthService;
import org.example.Server.SessionManager;

public class AuthController {

    private final AuthService authService;
    private final Gson gson = new Gson();

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Single routing entry point for the handler
    public ResponseDto route(RequestDto request) {
        try {
            switch (request.getRequest()) {
                case "login":
                    return handleLogin(request);
                case "register":
                    return handleRegister(request);
                case "logout":
                    return handleLogout(request);
                default:
                    return new ResponseDto(false, "Unknown request: " + request.getRequest(), null);
            }
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage(), null);
        }
    }

    private ResponseDto handleLogin(RequestDto request) {
        LoginRequestDto loginDto = gson.fromJson(request.getData(), LoginRequestDto.class);

        boolean success = authService.login(loginDto.getUsernameOrEmail(), loginDto.getPassword());
        if (!success) {
            return new ResponseDto(false, "Invalid credentials", null);
        }

        String token = SessionManager.createSession(loginDto.getUsernameOrEmail());
        UserResponseDto userDto = getUserByUsername(loginDto.getUsernameOrEmail());

        return new ResponseDto(true, "Login successful", gson.toJson(userDto));
    }

    private ResponseDto handleRegister(RequestDto request) throws Exception {
        RegisterRequestDto regDto = gson.fromJson(request.getData(), RegisterRequestDto.class);
        User user = authService.register(regDto.getUsername(), regDto.getEmail(), regDto.getPassword(), regDto.getRole());
        UserResponseDto userDto = new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt().toString(),
                user.getUpdatedAt().toString()
        );
        return new ResponseDto(true, "User registered", gson.toJson(userDto));
    }

    private ResponseDto handleLogout(RequestDto request) {
        if (request.getToken() != null && SessionManager.isValid(request.getToken())) {
            SessionManager.removeSession(request.getToken());
            return new ResponseDto(true, "Logout successful", null);
        } else {
            return new ResponseDto(false, "Invalid or missing token", null);
        }
    }

    public UserResponseDto getUserByUsername(String username) {
        User user = authService.getUserByUsername(username);
        if (user == null) return null;

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt().toString(),
                user.getUpdatedAt().toString()
        );
    }

    // Helper for login response
    private static class AuthResponseData {
        private final String token;
        private final UserResponseDto user;

        public AuthResponseData(String token, UserResponseDto user) {
            this.token = token;
            this.user = user;
        }
    }
}
