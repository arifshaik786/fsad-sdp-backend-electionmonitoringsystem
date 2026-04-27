package com.klef.sdp.electionmonitoringsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klef.sdp.electionmonitoringsystem.entity.DataAnalyst;

@Repository
public interface DataAnalystRepository extends JpaRepository<DataAnalyst, String>
{
    DataAnalyst findByEmailAndPassword(String email, String password);

    List<DataAnalyst> findByAssignedDistrict(String assignedDistrict);

    List<DataAnalyst> findByStatus(String status);
}
