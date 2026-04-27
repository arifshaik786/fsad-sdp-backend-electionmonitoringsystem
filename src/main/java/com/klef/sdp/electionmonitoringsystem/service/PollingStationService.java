package com.klef.sdp.electionmonitoringsystem.service;

import java.util.List;

import com.klef.sdp.electionmonitoringsystem.entity.PollingStation;

public interface PollingStationService
{
    String addPollingStation(PollingStation station);

    List<PollingStation> getAllPollingStations();

    PollingStation getPollingStationById(Long id);

    String deletePollingStation(Long id);

    List<PollingStation> getPollingStationsByDistrict(String district);
}
