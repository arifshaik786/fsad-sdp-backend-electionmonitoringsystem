package com.klef.sdp.electionmonitoringsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.klef.sdp.electionmonitoringsystem.entity.Citizen;
import com.klef.sdp.electionmonitoringsystem.entity.Issue;
import com.klef.sdp.electionmonitoringsystem.entity.RefreshToken;
import com.klef.sdp.electionmonitoringsystem.service.CitizenService;
import com.klef.sdp.electionmonitoringsystem.service.IssueService;
import com.klef.sdp.electionmonitoringsystem.service.RefreshTokenService;
import com.klef.sdp.electionmonitoringsystem.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/citizenapi")
@CrossOrigin("*")
public class CitizenController {

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @GetMapping("/")
    public String home() {
        return "Citizen API is running";
    }

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> registerCitizen(@RequestBody Citizen citizen) {
        try {
            if (citizen == null || citizen.getUsername() == null ||
                citizen.getAadhaarNumber() == null || citizen.getPassword() == null) {
                return ResponseEntity.badRequest().body("All fields are required");
            }

            String message = citizenService.registerCitizen(citizen);

            if (message.contains("exists") || message.contains("registered")) {
                return ResponseEntity.status(409).body(message);
            }

            return ResponseEntity.status(201).body(message);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> loginCitizen(@RequestBody Citizen citizen, HttpServletResponse httpResponse) {
        try {
            Citizen user = citizenService.verifyCitizenLogin(
                    citizen.getUsername(), citizen.getPassword());

            if (user == null) {
                return ResponseEntity.status(401).body("Invalid credentials");
            }

            String token = jwtUtil.generateToken(user.getUsername(), "citizen");

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername(), "citizen");
            
            Cookie cookie = new Cookie("refreshToken", refreshToken.getTokenHash());
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/auth/");
            cookie.setMaxAge(7 * 24 * 60 * 60);
            httpResponse.addCookie(cookie);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", token);
            response.put("user", user);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    // ✅ PROFILE
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestParam String username) {
        Citizen citizen = citizenService.getCitizenByUsername(username);
        return citizen != null ?
                ResponseEntity.ok(citizen) :
                ResponseEntity.status(404).body("Not found");
    }

    // ✅ UPDATE PROFILE
    @PutMapping("/profile/update")
    public ResponseEntity<?> updateProfile(@RequestBody Citizen citizen) {
        Citizen updated = citizenService.updateCitizenProfile(citizen);
        return updated != null ?
                ResponseEntity.ok(updated) :
                ResponseEntity.status(404).body("Citizen not found");
    }

    // ================= ISSUE APIs =================

    @PostMapping("/report")
    public ResponseEntity<?> reportIssue(@RequestBody Issue issue) {
        try {
            if (issue == null) {
                return ResponseEntity.badRequest().body("Issue data required");
            }
            return ResponseEntity.ok(issueService.reportIssue(issue));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error reporting issue");
        }
    }

    @GetMapping("/issues")
    public ResponseEntity<List<Issue>> getAllIssues() {
        return ResponseEntity.ok(issueService.getAllIssues());
    }

    @GetMapping("/issue/{id}")
    public ResponseEntity<?> getIssue(@PathVariable int id) {
        Issue issue = issueService.getIssueById(id);
        return issue != null ?
                ResponseEntity.ok(issue) :
                ResponseEntity.status(404).body("Issue not found");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteIssue(@PathVariable int id) {
        return ResponseEntity.ok(issueService.deleteIssue(id));
    }
}