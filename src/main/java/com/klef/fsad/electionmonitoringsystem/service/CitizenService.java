package com.klef.fsad.electionmonitoringsystem.service;

import com.klef.fsad.electionmonitoringsystem.entity.Citizen;

public interface CitizenService 
{
    public String registerCitizen(Citizen citizen);
    public Citizen verifyCitizenLogin(String username, String password);
    public Citizen getCitizenByUsername(String username);
    public Citizen getCitizenByAadhaar(String aadhaarNumber);
    Citizen updateCitizenProfile(Citizen citizen);
  

    // Polling Station specific
    public Object getAllPollingStations();
    public Object getPollingStationsByDistrict(String district);
    public Object getPollingStationsByState(String state);
    
}