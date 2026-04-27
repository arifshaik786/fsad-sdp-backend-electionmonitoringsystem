package com.klef.sdp.electionmonitoringsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dataanalyst_table")
public class DataAnalyst
{
    @Id
    @Column(length = 100)
    private String email;

    @Column(length = 50, nullable = false)
    private String password;

    @Column(length = 150)
    private String analystName;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String expertise;

    @Column(length = 100)
    private String assignedDistrict;

    @Column(length = 20)
    private String status;

    @Column(length = 20)
    private String role = "DATA_ANALYST";

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

    public String getAnalystName() {
        return analystName;
    }

    public void setAnalystName(String analystName) {
        this.analystName = analystName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getAssignedDistrict() {
        return assignedDistrict;
    }

    public void setAssignedDistrict(String assignedDistrict) {
        this.assignedDistrict = assignedDistrict;
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
        return "DataAnalyst [email=" + email + ", analystName=" + analystName + ", role=" + role + "]";
    }
}
