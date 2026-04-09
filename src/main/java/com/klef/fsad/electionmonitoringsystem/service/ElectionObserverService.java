package com.klef.fsad.electionmonitoringsystem.service;

import java.util.List;

import com.klef.fsad.electionmonitoringsystem.entity.ElectionObserver;
import com.klef.fsad.electionmonitoringsystem.entity.PollingStation;

public interface ElectionObserverService
{
    String registerElectionObserver(ElectionObserver electionObserver);

    ElectionObserver verifyElectionObserverLogin(String email, String password);

    List<ElectionObserver> getAllElectionObservers();

    ElectionObserver getElectionObserverByEmail(String email);

    String deleteElectionObserver(String email);

    String assignStation(String email, String assignedStation);

    // Polling Station specific
    public List<PollingStation> getPollingStationsByDistrict(String district);
    public Object getPollingStationByName(String stationName);
    ElectionObserver updateElectionObserverProfile(ElectionObserver observer);
}
