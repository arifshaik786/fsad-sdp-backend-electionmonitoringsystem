package com.klef.sdp.electionmonitoringsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.sdp.electionmonitoringsystem.entity.ElectionObserver;
import com.klef.sdp.electionmonitoringsystem.entity.PollingStation;
import com.klef.sdp.electionmonitoringsystem.repository.ElectionObserverRepository;
import com.klef.sdp.electionmonitoringsystem.repository.PollingStationRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class ElectionObserverServiceImpl implements ElectionObserverService
{
    @Autowired
    private ElectionObserverRepository electionObserverRepository;

    @Autowired
    private PollingStationRepository pollingStationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String registerElectionObserver(ElectionObserver electionObserver)
    {
        if (electionObserverRepository.existsById(electionObserver.getEmail()))
        {
            return "Election observer already exists";
        }
        electionObserver.setRole("ELECTION_OBSERVER");
        electionObserver.setPassword(passwordEncoder.encode(electionObserver.getPassword()));
        electionObserverRepository.save(electionObserver);
        return "Election observer registered successfully";
    }

    @Override
    public ElectionObserver verifyElectionObserverLogin(String email, String password)
    {
        Optional<ElectionObserver> optional = electionObserverRepository.findById(email);
        if (optional.isPresent()) {
            ElectionObserver observer = optional.get();
            if (passwordEncoder.matches(password, observer.getPassword())) {
                return observer;
            }
        }
        return null;
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
    @Override
    public ElectionObserver updateElectionObserverProfile(ElectionObserver observer) {
        Optional<ElectionObserver> optional = electionObserverRepository.findById(observer.getEmail());

        if (optional.isEmpty()) {
            return null;
        }

        ElectionObserver existing = optional.get();
        if (observer.getPassword() != null && !observer.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(observer.getPassword()));
        }

        return electionObserverRepository.save(existing);
    }
}
