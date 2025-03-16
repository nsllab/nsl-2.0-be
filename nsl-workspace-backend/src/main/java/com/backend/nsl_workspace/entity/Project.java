package com.backend.nsl_workspace.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_tbl")
public class Project {
    @Id
    @Column(name = "project_id")
    private String projectId;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "project_desc")
    private String projectDesc;

    @Column(name = "research_area_id")
    private Integer researchAreaId;

    @Column(name = "proj_img_path")
    private String projImgPath;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "funding_id")
    private Integer fundingId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
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