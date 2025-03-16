package com.backend.nsl_workspace.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class ProjectMemberDTO {

    private String projectId;
    private Integer userId;
    private String role;
    private LocalDateTime joinDate;
    private LocalDateTime  endDate;

    // Getters and Setters
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime  getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime  joinDate) {
        this.joinDate = joinDate;
    }

    public LocalDateTime  getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime  endDate) {
        this.endDate = endDate;
    }
}
