package com.klef.fsad.electionmonitoringsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.klef.fsad.electionmonitoringsystem.entity.Issue;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Integer> {
}