package com.klef.sdp.electionmonitoringsystem.controller;

import com.klef.sdp.electionmonitoringsystem.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/otp")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowCredentials = "true")
public class OTPController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendOTP(@RequestParam String email) {
        try {
            emailService.sendOTP(email);
            Map<String, String> response = new HashMap<>();
            response.put("message", "OTP sent successfully to " + email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending OTP");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOTP(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = emailService.verifyOTP(email, otp);
        if (isValid) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "OTP verified successfully");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body("Invalid OTP");
        }
    }
}
