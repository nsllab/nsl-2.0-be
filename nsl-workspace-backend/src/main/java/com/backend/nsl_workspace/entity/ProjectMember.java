package com.backend.nsl_workspace.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@IdClass(ProjectMemberId.class)
@Table(name = "project_member_tbl") // Matches the table name in the database
public class ProjectMember implements Serializable {

    @Id
    @Column(name = "project_id")
    private String projectId;

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "ENUM('Lead', 'Member')")
    private Role role;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "join_date", columnDefinition = "DATETIME(6)")
    private LocalDateTime joinDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date", columnDefinition = "DATETIME(6)")
    private LocalDateTime endDate;

    // Default constructor
    public ProjectMember() {}

    // Parameterized constructor
    public ProjectMember(String projectId, Integer userId, Role role, LocalDateTime joinDate, LocalDateTime endDate) {
        this.projectId = projectId;
        this.userId = userId;
        this.role = role;
        this.joinDate = joinDate;
        this.endDate = endDate;
    }

    // Getters
    public String getProjectId() {
        return projectId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    // Setters
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    // Enum for Role
    public enum Role {
        Lead,
        Member
    }
}
