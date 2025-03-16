package com.backend.nsl_workspace.entity;

import java.io.Serializable;
import java.util.Objects;

public class ProjectMemberId implements Serializable {

    private String projectId;
    private Integer userId;

    // Default constructor
    public ProjectMemberId() {}

    // Parameterized constructor
    public ProjectMemberId(String projectId, Integer userId) {
        this.projectId = projectId;
        this.userId = userId;
    }

    // Getters
    public String getProjectId() {
        return projectId;
    }

    public Integer getUserId() {
        return userId;
    }

    // Setters
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectMemberId that = (ProjectMemberId) o;
        return Objects.equals(projectId, that.projectId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, userId);
    }
}
