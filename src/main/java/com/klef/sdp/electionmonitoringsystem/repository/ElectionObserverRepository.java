package com.klef.sdp.electionmonitoringsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klef.sdp.electionmonitoringsystem.entity.ElectionObserver;

@Repository
public interface ElectionObserverRepository extends JpaRepository<ElectionObserver, String>
{
    ElectionObserver findByEmailAndPassword(String email, String password);

    List<ElectionObserver> findByDistrict(String district);

    List<ElectionObserver> findByAssignedStation(String assignedStation);

    List<ElectionObserver> findByStatus(String status);
}
