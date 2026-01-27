package com.inventory.auth.controller;

import com.inventory.auth.model.*;
import com.inventory.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // SIGNUP - Anyone can signup and choose their role (ADMIN or STAFF)
    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest request) {
        // Convert DTO to User entity
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        
        // No admin credentials required - anyone can signup
        return authService.registerUser(user, null, null);
    }

    // SIGNIN
    @PostMapping("/signin")
    public AuthResponse signin(@RequestBody LoginRequest request) {
        return authService.authenticate(request.getUsername(), request.getPassword());
    }

    // FORGET PASSWORD
    @PostMapping("/forget-password")
    public String forgetPassword(@RequestBody ForgotPasswordRequest request) {
        return authService.processForgetPassword(request.getEmail());
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {
        // Support both token-based and email-based reset
        if (request.getToken() != null && !request.getToken().isEmpty()) {
            return authService.resetPassword(request.getToken(), request.getNewPassword());
        } else if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            return authService.resetPasswordWithEmail(request.getEmail(), request.getNewPassword());
        }
        return "Invalid request";
    }

    // Legacy OTP endpoints preserved (optional use)
    @PostMapping("/send-otp")
    public String sendOtp(@RequestBody User user) {
        return authService.sendOtp(user.getEmail());
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody User user) {
        return authService.verifyOtp(user.getEmail(), user.getOtp());
    }
}
