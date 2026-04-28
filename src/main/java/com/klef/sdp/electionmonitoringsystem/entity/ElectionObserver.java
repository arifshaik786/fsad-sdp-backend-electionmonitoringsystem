package com.klef.sdp.electionmonitoringsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "electionobserver_table")
public class ElectionObserver
{
    @Id
    @Column(length = 100)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 150)
    private String observerName;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String district;

    @Column(length = 100)
    private String assignedStation;

    @Column(length = 20)
    private String status;

    @Column(length = 20)
    private String role = "ELECTION_OBSERVER";

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getObserverName() {
        return observerName;
    }

    public void setObserverName(String observerName) {
        this.observerName = observerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAssignedStation() {
        return assignedStation;
    }

    public void setAssignedStation(String assignedStation) {
        this.assignedStation = assignedStation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "ElectionObserver [email=" + email + ", observerName=" + observerName + ", role=" + role + "]";
    }
}
