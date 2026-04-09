package com.klef.fsad.electionmonitoringsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.klef.fsad.electionmonitoringsystem.entity.Citizen;
import com.klef.fsad.electionmonitoringsystem.entity.Issue;
import com.klef.fsad.electionmonitoringsystem.service.CitizenService;
import com.klef.fsad.electionmonitoringsystem.service.IssueService;

@RestController
@RequestMapping("/citizenapi")
@CrossOrigin("*")
public class CitizenController {

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private IssueService issueService;

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
    public ResponseEntity<?> loginCitizen(@RequestBody Citizen citizen) {
        try {
            Citizen user = citizenService.verifyCitizenLogin(
                    citizen.getUsername(), citizen.getPassword());

            if (user == null) {
                return ResponseEntity.status(401).body("Invalid credentials");
            }

            return ResponseEntity.ok(user);

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