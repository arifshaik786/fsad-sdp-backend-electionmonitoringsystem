package com.klef.sdp.electionmonitoringsystem.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String issueType;

    @Column(nullable = false)
    private String pollingStation;

    @Column(nullable = false)
    private String severity;

    @Column(nullable = false, length = 1000)
    private String description;

    private String evidence; // file path or file name

    private String status; // Pending, Resolved

    private LocalDateTime createdAt;

    // Constructors
    public Issue() {}

    public Issue(String issueType, String pollingStation, String severity, String description, String evidence) {
        this.issueType = issueType;
        this.pollingStation = pollingStation;
        this.severity = severity;
        this.description = description;
        this.evidence = evidence;
        this.status = "Pending";
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() { return id; }

    public String getIssueType() { return issueType; }
    public void setIssueType(String issueType) { this.issueType = issueType; }

    public String getPollingStation() { return pollingStation; }
    public void setPollingStation(String pollingStation) { this.pollingStation = pollingStation; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEvidence() { return evidence; }
    public void setEvidence(String evidence) { this.evidence = evidence; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}