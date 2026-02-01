package com.inventory.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.reset.base-url:http://localhost:8081/reset-password?token=}")
    private String resetBaseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Password Reset OTP");
            message.setText("Your OTP for password reset is: " + otp);

            mailSender.send(message);
            logger.info("OTP email sent successfully to: {}", toEmail);
        } catch (MailException e) {
            logger.error("Failed to send OTP email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send OTP email. Please try again later.");
        }
    }

    public void sendResetLink(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Password Reset Link");
            message.setText("Reset your password using the following link: " + resetBaseUrl + token);
            
            mailSender.send(message);
            logger.info("Reset link email sent successfully to: {}", toEmail);
        } catch (MailException e) {
            logger.error("Failed to send reset link email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send reset email. Please try again later.");
        }
    }

    /**
     * Sends a low-stock alert email to the supplier with requested quantity.
     */
    public void sendLowStockAlertToSupplier(String toEmail, String supplierName, String productName,
                                            String categoryName, int quantityRequested) {
        if (toEmail == null || toEmail.isBlank()) {
            throw new IllegalArgumentException("Supplier email is required to send alert.");
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Low Stock Alert - " + productName);
            String body = "Dear " + supplierName + ",\n\n"
                    + "There is low stock in this electronics company and the qty : " + quantityRequested + "\n\n"
                    + "Product: " + productName + "\n"
                    + "Category: " + (categoryName != null ? categoryName : "N/A") + "\n\n"
                    + "Please arrange supply at the earliest.";
            message.setText(body);

            mailSender.send(message);
            logger.info("Low stock alert email sent to supplier {} for product {}", toEmail, productName);
        } catch (MailException e) {
            logger.error("Failed to send low stock alert to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send email to supplier. Please try again later.");
        }
    }
}
