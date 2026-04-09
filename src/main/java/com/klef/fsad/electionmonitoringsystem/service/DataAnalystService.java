package com.klef.fsad.electionmonitoringsystem.service;

import java.util.List;

import com.klef.fsad.electionmonitoringsystem.entity.DataAnalyst;

public interface DataAnalystService
{
    String registerDataAnalyst(DataAnalyst dataAnalyst);

    DataAnalyst verifyDataAnalystLogin(String email, String password);

    List<DataAnalyst> getAllDataAnalysts();

    DataAnalyst getDataAnalystByEmail(String email);

    String deleteDataAnalyst(String email);

    String assignDistrict(String email, String district);

    // Polling Station specific
    public Object getPollingStationsByDistrict(String district);

    public Object getAllPollingStations();
    
    DataAnalyst updateDataAnalystProfile(DataAnalyst analyst);
}
