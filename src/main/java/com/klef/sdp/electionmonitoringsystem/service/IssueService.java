package com.klef.sdp.electionmonitoringsystem.service;

import java.util.List;

import com.klef.sdp.electionmonitoringsystem.entity.Issue;

public interface IssueService {

    Issue reportIssue(Issue issue);

    List<Issue> getAllIssues();

    Issue getIssueById(int id);

    String deleteIssue(int id);
}