package com.klef.sdp.electionmonitoringsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.sdp.electionmonitoringsystem.entity.Citizen;
import com.klef.sdp.electionmonitoringsystem.entity.PollingStation;
import com.klef.sdp.electionmonitoringsystem.repository.CitizenRepository;
import com.klef.sdp.electionmonitoringsystem.repository.PollingStationRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@Service
public class CitizenServiceImpl implements CitizenService
{
    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private PollingStationRepository pollingStationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String registerCitizen(Citizen citizen) 
    {
        if(citizenRepository.findByUsername(citizen.getUsername()) != null)
        {
            return "Username already exists!";
        }

        if(citizenRepository.findByAadhaarNumber(citizen.getAadhaarNumber()) != null)
        {
            return "Aadhaar already registered!";
        }

        citizen.setRole("CITIZEN");
        citizen.setPassword(passwordEncoder.encode(citizen.getPassword()));
        citizenRepository.save(citizen);
        return "Citizen Registered Successfully";
    }

    @Override
    public Citizen verifyCitizenLogin(String username, String password) 
    {
        Citizen citizen = citizenRepository.findByUsername(username);
        if (citizen != null && passwordEncoder.matches(password, citizen.getPassword())) {
            return citizen;
        }
        return null;
    }

    @Override
    public Citizen getCitizenByUsername(String username) 
    {
        return citizenRepository.findByUsername(username);
    }

    @Override
    public Citizen getCitizenByAadhaar(String aadhaarNumber) 
    {
        return citizenRepository.findByAadhaarNumber(aadhaarNumber);
    }

    @Override
    public List<PollingStation> getAllPollingStations()
    {
        return pollingStationRepository.findAll();
    }

    @Override
    public List<PollingStation> getPollingStationsByDistrict(String district)
    {
        return pollingStationRepository.findByDistrict(district);
    }

    @Override
    public List<PollingStation> getPollingStationsByState(String state)
    {
        return pollingStationRepository.findByState(state);
    }
    @Override
    public Citizen updateCitizenProfile(Citizen citizen) {
        Citizen existing = citizenRepository.findByUsername(citizen.getUsername());

        if (existing == null) {
            return null;
        }

        if (citizen.getPassword() != null && !citizen.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(citizen.getPassword()));
        }
        if (citizen.getAadhaarNumber() != null) {
            existing.setAadhaarNumber(citizen.getAadhaarNumber());
        }

        return citizenRepository.save(existing);
    }
}