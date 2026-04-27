package com.klef.sdp.electionmonitoringsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klef.sdp.electionmonitoringsystem.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, String>
{
    Admin findByEmailAndPassword(String email, String password);
}