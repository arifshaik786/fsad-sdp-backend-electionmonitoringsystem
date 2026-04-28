package com.klef.sdp.electionmonitoringsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    private Map<String, String> otpStorage = new HashMap<>();

    public String sendOTP(String toEmail) {
        String otp = generateOTP();
        otpStorage.put(toEmail, otp);
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Registration OTP - Election Monitoring System");
        message.setText("Welcome to the Election Monitoring System. Your OTP is: " + otp);
        
        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + toEmail + ": " + e.getMessage());
        }
        return otp;
    }
    
    public boolean verifyOTP(String email, String otp) {
        if(otpStorage.containsKey(email) && otpStorage.get(email).equals(otp)) {
            otpStorage.remove(email); // OTP can only be used once
            return true;
        }
        return false;
    }

    private String generateOTP() {
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }
}
