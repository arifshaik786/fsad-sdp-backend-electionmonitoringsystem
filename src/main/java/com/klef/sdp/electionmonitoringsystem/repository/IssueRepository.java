package com.klef.sdp.electionmonitoringsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klef.sdp.electionmonitoringsystem.entity.Issue;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Integer> {
}