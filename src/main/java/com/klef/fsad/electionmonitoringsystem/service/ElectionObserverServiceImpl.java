package com.klef.fsad.electionmonitoringsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.fsad.electionmonitoringsystem.entity.ElectionObserver;
import com.klef.fsad.electionmonitoringsystem.repository.ElectionObserverRepository;
import com.klef.fsad.electionmonitoringsystem.repository.PollingStationRepository;
import com.klef.fsad.electionmonitoringsystem.entity.PollingStation;

@Service
public class ElectionObserverServiceImpl implements ElectionObserverService
{
    @Autowired
    private ElectionObserverRepository electionObserverRepository;

    @Autowired
    private PollingStationRepository pollingStationRepository;

    @Override
    public String registerElectionObserver(ElectionObserver electionObserver)
    {
        if (electionObserverRepository.existsById(electionObserver.getEmail()))
        {
            return "Election observer already exists";
        }
        electionObserver.setRole("ELECTION_OBSERVER");
        electionObserverRepository.save(electionObserver);
        return "Election observer registered successfully";
    }

    @Override
    public ElectionObserver verifyElectionObserverLogin(String email, String password)
    {
        return electionObserverRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public List<ElectionObserver> getAllElectionObservers()
    {
        return electionObserverRepository.findAll();
    }

    @Override
    public ElectionObserver getElectionObserverByEmail(String email)
    {
        Optional<ElectionObserver> optional = electionObserverRepository.findById(email);
        return optional.orElse(null);
    }

    @Override
    public String deleteElectionObserver(String email)
    {
        if (!electionObserverRepository.existsById(email))
        {
            return "Election observer not found";
        }
        electionObserverRepository.deleteById(email);
        return "Election observer deleted successfully";
    }

    @Override
    public String assignStation(String email, String assignedStation)
    {
        Optional<ElectionObserver> optional = electionObserverRepository.findById(email);
        if (optional.isEmpty())
        {
            return "Election observer not found";
        }
        ElectionObserver observer = optional.get();
        observer.setAssignedStation(assignedStation);
        electionObserverRepository.save(observer);
        return "Station assigned successfully";
    }

    @Override
    public PollingStation getPollingStationByName(String stationName)
    {
        return pollingStationRepository.findByStationName(stationName);
    }

    @Override
    public List<PollingStation> getPollingStationsByDistrict(String district)
    {
        return pollingStationRepository.findByDistrict(district);
    }
}
