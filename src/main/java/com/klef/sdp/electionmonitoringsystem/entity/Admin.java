package com.klef.sdp.electionmonitoringsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="admin_table")
public class Admin 
{
   @Id
   @Column(length = 50)
   private String email;

   @Column(length = 255, nullable = false)
   private String password;

   @Column(length = 20)
   private String role = "ADMIN";

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

   public String getRole() {
      return role;
   }

   public void setRole(String role) {
      this.role = role;
   }

   @Override
   public String toString() {
      return "Admin [email=" + email + ", role=" + role + "]";
   }
}