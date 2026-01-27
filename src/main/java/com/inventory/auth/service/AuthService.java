package com.inventory.auth.service;

import com.inventory.auth.model.AuthResponse;
import com.inventory.auth.model.User;
import com.inventory.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private EmailService emailService;

    // SIGNUP - Anyone can signup and choose their role (ADMIN or STAFF)
    public String registerUser(User userDetails, String adminUsername, String adminPassword) {

        if (userRepository.findByUsername(userDetails.getUsername()) != null) {
            return "User already exists";
        }
        if (userDetails.getEmail() != null && userRepository.findByEmail(userDetails.getEmail()) != null) {
            return "Email already registered";
        }

        boolean hasAdmin = userRepository.existsByRoleIgnoreCase("ADMIN");
        String requestedRole = userDetails.getRole() != null ? userDetails.getRole().trim() : null;
        
        // Normalize role: "admin" -> "ADMIN", "employee"/"staff" -> "STAFF", empty/null -> "STAFF"
        // Only two roles allowed: ADMIN and STAFF
        if (requestedRole == null || requestedRole.isEmpty()) {
            requestedRole = "STAFF";
        } else if ("admin".equalsIgnoreCase(requestedRole)) {
            requestedRole = "ADMIN";
        } else if ("employee".equalsIgnoreCase(requestedRole) || "staff".equalsIgnoreCase(requestedRole)) {
            requestedRole = "STAFF";
        } else {
            requestedRole = requestedRole.toUpperCase();
            // Validate only ADMIN or STAFF roles are allowed
            if (!"ADMIN".equals(requestedRole) && !"STAFF".equals(requestedRole)) {
                requestedRole = "STAFF"; // Default to STAFF for invalid roles
            }
        }
        
        // Allow anyone to signup with their chosen role (ADMIN or STAFF)
        // No admin credential requirement - users can choose their role freely
        if (!hasAdmin && "STAFF".equals(requestedRole)) {
            // If no admin exists and user chose STAFF, make them ADMIN (first user must be admin)
            requestedRole = "ADMIN";
        }
        
        userDetails.setRole(requestedRole);

        userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        userRepository.save(userDetails);
        return "Signup successful";
    }

    // SIGNIN
    public AuthResponse authenticate(String username, String password) {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new AuthResponse(false, "Invalid username");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return new AuthResponse(false, "Invalid password");
        }

        String token = jwtUtility.generateToken(user);
        return new AuthResponse(true, "Login successful", token, user.getUsername(), user.getEmail(), user.getRole());
    }

    // FORGET PASSWORD
    public String processForgetPassword(String email) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "Email not registered";
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);

        try {
            emailService.sendResetLink(email, token);
        } catch (Exception e) {
            return "Failed to send reset email. Please try again later.";
        }
        return "Reset link sent";
    }

    public String resetPassword(String token, String newPassword) {

        User user = userRepository.findByResetToken(token);
        if (user == null || user.getResetTokenExpiry() == null ||
                LocalDateTime.now().isAfter(user.getResetTokenExpiry())) {
            return "Invalid token";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
        return "Password reset successful";
    }

    // OTP helpers kept for compatibility (not primary flow)
    private String generateOtp() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    public String sendOtp(String email) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "EMAIL_NOT_REGISTERED";
        }

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());

        userRepository.save(user);
        
        try {
            emailService.sendOtpEmail(email, otp);
        } catch (Exception e) {
            return "EMAIL_SEND_FAILED";
        }

        return "OTP_SENT";
    }

    public String verifyOtp(String email, String otp) {

        User user = userRepository.findByEmail(email);
        if (user == null) return "INVALID_EMAIL";

        if (!otp.equals(user.getOtp())) return "INVALID_OTP";

        return "OTP_VERIFIED";
    }

    // Reset password using email (after OTP verification)
    public String resetPasswordWithEmail(String email, String newPassword) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "INVALID_EMAIL";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtp(null); // Clear OTP after use
        user.setOtpGeneratedTime(null);
        userRepository.save(user);
        return "PASSWORD_RESET_SUCCESS";
    }
}
