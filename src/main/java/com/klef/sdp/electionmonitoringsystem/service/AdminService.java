package com.klef.sdp.electionmonitoringsystem.service;

import java.util.List;

import com.klef.sdp.electionmonitoringsystem.entity.Admin;
import com.klef.sdp.electionmonitoringsystem.entity.DataAnalyst;
import com.klef.sdp.electionmonitoringsystem.entity.ElectionObserver;
import com.klef.sdp.electionmonitoringsystem.entity.PollingStation;

public interface AdminService
{
    // ── Admin Auth ──────────────────────────────────────────────
    Admin verifyAdminLogin(String email, String password);

    String registerAdmin(Admin admin);

    // ── Polling Stations ────────────────────────────────────────
    String addPollingStation(PollingStation station);

    List<PollingStation> getAllPollingStations();

    PollingStation getPollingStationById(Long id);

    String deletePollingStation(Long id);

    List<PollingStation> getPollingStationsByDistrict(String district);

    // ── Data Analysts ───────────────────────────────────────────
    DataAnalyst addDataAnalyst(DataAnalyst dataAnalyst);

    List<DataAnalyst> getAllDataAnalysts();

    DataAnalyst getDataAnalystByEmail(String email);

    String deleteDataAnalyst(String email);

    String assignDistrictToAnalyst(String email, String district);

    // ── Election Observers ──────────────────────────────────────
    ElectionObserver addElectionObserver(ElectionObserver electionObserver);

    List<ElectionObserver> getAllElectionObservers();

    ElectionObserver getElectionObserverByEmail(String email);

    String deleteElectionObserver(String email);

    String assignStationToObserver(String email, String assignedStation);

    String assignDistrictToObserver(String email, String district);
}
