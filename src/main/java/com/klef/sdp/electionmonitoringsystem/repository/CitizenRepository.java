package com.klef.sdp.electionmonitoringsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klef.sdp.electionmonitoringsystem.entity.Citizen;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, String>
{
    Citizen findByUsername(String username);
    Citizen findByAadhaarNumber(String AadhaarNumber);
    Citizen findByUsernameAndPassword(String username, String password);
}