package com.backend.nsl_workspace.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProjectDTO {
    private String projectId;
    private String projectName;
    private String projectDesc;
    private Integer researchAreaId;
    private String projImgPath;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer fundingId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters
    public String getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectDesc() {
        return projectDesc;
    }

    public Integer getResearchAreaId() {
        return researchAreaId;
    }

    public String getProjImgPath() {
        return projImgPath;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Integer getFundingId() {
        return fundingId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setProjectDesc(String projectDesc) {
        this.projectDesc = projectDesc;
    }

    public void setResearchAreaId(Integer researchAreaId) {
        this.researchAreaId = researchAreaId;
    }

    public void setProjImgPath(String projImgPath) {
        this.projImgPath = projImgPath;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setFundingId(Integer fundingId) {
        this.fundingId = fundingId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}