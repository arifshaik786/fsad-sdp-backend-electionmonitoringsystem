package com.klef.fsad.electionmonitoringsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.klef.fsad.electionmonitoringsystem.entity.Citizen;
import com.klef.fsad.electionmonitoringsystem.service.CitizenService;

@RestController
@RequestMapping("/citizenapi")
@CrossOrigin("*")
public class CitizenController 
{
    @Autowired
    private CitizenService citizenService;

    @GetMapping("/")
    public String home() 
    {
        return "Citizen API is running";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCitizen(@RequestBody Citizen citizen) 
    {
        try 
        {
            if (citizen == null || 
                citizen.getUsername() == null || 
                citizen.getAadhaarNumber() == null || 
                citizen.getPassword() == null) 
            {
                return ResponseEntity.badRequest().body("All fields are required");
            }

            citizen.setUsername(citizen.getUsername().trim());
            citizen.setAadhaarNumber(citizen.getAadhaarNumber().trim());
            citizen.setPassword(citizen.getPassword().trim());

            String message = citizenService.registerCitizen(citizen);

            if ("Username already exists!".equals(message) || 
                "Aadhaar already registered!".equals(message)) 
            {
                return ResponseEntity.status(409).body(message);
            }

            return ResponseEntity.status(201).body(message);
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginCitizen(@RequestBody Citizen citizen) 
    {
        try 
        {
            if (citizen == null || 
                citizen.getUsername() == null || 
                citizen.getPassword() == null) 
            {
                return ResponseEntity.badRequest().body("Username and password are required");
            }

            String username = citizen.getUsername().trim();
            String password = citizen.getPassword().trim();

            Citizen authenticatedCitizen = citizenService.verifyCitizenLogin(username, password);

            if (authenticatedCitizen == null) 
            {
                return ResponseEntity.status(401).body("Invalid username or password");
            }

            return ResponseEntity.ok().body(authenticatedCitizen);
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/getbyusername")
    public ResponseEntity<?> getCitizenByUsername(@RequestParam String username) 
    {
        try 
        {
            Citizen citizen = citizenService.getCitizenByUsername(username.trim());

            if (citizen == null) 
            {
                return ResponseEntity.status(404).body("Citizen not found");
            }

            return ResponseEntity.ok(citizen);
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/getbyaadhaar")
    public ResponseEntity<?> getCitizenByAadhaar(@RequestParam String aadhaar) 
    {
        try 
        {
            Citizen citizen = citizenService.getCitizenByAadhaar(aadhaar.trim());

            if (citizen == null) 
            {
                return ResponseEntity.status(404).body("Citizen not found");
            }

            return ResponseEntity.ok(citizen);
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}