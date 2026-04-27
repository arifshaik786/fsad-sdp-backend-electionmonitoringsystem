package com.klef.sdp.electionmonitoringsystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.sdp.electionmonitoringsystem.entity.Issue;
import com.klef.sdp.electionmonitoringsystem.repository.IssueRepository;

@Service
public class IssueServiceImpl implements IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Override
    public Issue reportIssue(Issue issue) {
        issue.setStatus("Pending");
        issue.setCreatedAt(LocalDateTime.now());
        return issueRepository.save(issue);
    }

    @Override
    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    @Override
    public Issue getIssueById(int id) {
        return issueRepository.findById(id).orElse(null);
    }

    @Override
    public String deleteIssue(int id) {
        issueRepository.deleteById(id);
        return "Issue Deleted Successfully";
    }
}