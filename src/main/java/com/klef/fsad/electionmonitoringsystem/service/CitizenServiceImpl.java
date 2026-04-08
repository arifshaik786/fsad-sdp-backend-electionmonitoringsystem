package com.klef.fsad.electionmonitoringsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.fsad.electionmonitoringsystem.entity.Citizen;
import com.klef.fsad.electionmonitoringsystem.repository.CitizenRepository;
import com.klef.fsad.electionmonitoringsystem.repository.PollingStationRepository;
import java.util.List;
import com.klef.fsad.electionmonitoringsystem.entity.PollingStation;

@Service
public class CitizenServiceImpl implements CitizenService
{
    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private PollingStationRepository pollingStationRepository;

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
        citizenRepository.save(citizen);
        return "Citizen Registered Successfully";
    }

    @Override
    public Citizen verifyCitizenLogin(String username, String password) 
    {
        return citizenRepository.findByUsernameAndPassword(username, password);
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
}