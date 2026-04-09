package com.klef.fsad.electionmonitoringsystem.service;

import java.util.List;
import com.klef.fsad.electionmonitoringsystem.entity.Issue;

public interface IssueService {

    Issue reportIssue(Issue issue);

    List<Issue> getAllIssues();

    Issue getIssueById(int id);

    String deleteIssue(int id);
}