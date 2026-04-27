package com.klef.sdp.electionmonitoringsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.sdp.electionmonitoringsystem.entity.PollingStation;
import com.klef.sdp.electionmonitoringsystem.repository.PollingStationRepository;

@Service
public class PollingStationServiceImpl implements PollingStationService
{
    @Autowired
    private PollingStationRepository pollingStationRepository;

    @Override
    public String addPollingStation(PollingStation station)
    {
        pollingStationRepository.save(station);
        return "Polling station added successfully";
    }

    @Override
    public List<PollingStation> getAllPollingStations()
    {
        return pollingStationRepository.findAll();
    }

    @Override
    public PollingStation getPollingStationById(Long id)
    {
        Optional<PollingStation> optional = pollingStationRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public String deletePollingStation(Long id)
    {
        if (!pollingStationRepository.existsById(id))
        {
            return "Polling station not found";
        }
        pollingStationRepository.deleteById(id);
        return "Polling station deleted successfully";
    }

    @Override
    public List<PollingStation> getPollingStationsByDistrict(String district)
    {
        return pollingStationRepository.findByDistrict(district);
    }
}
