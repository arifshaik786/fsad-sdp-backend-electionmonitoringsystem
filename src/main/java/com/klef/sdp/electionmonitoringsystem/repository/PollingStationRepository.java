package com.klef.sdp.electionmonitoringsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klef.sdp.electionmonitoringsystem.entity.PollingStation;

@Repository
public interface PollingStationRepository extends JpaRepository<PollingStation, Long>
{
    List<PollingStation> findByDistrict(String district);

    List<PollingStation> findByState(String state);

    PollingStation findByStationName(String stationName);
}
