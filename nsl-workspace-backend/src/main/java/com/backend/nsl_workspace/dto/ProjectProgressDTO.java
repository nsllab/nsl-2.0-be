package com.backend.nsl_workspace.dto;

import com.backend.nsl_workspace.entity.ProjectProgress;
import java.time.LocalDateTime;

public class ProjectProgressDTO {

    private Integer progressId;
    private Integer reportId;
    private String projectId;
    private String prevWeekProg;
    private String thisWeekProg;
    private LocalDateTime createdAt;

    // Default constructor
    public ProjectProgressDTO() {
    }

    // Constructor with all fields
    public ProjectProgressDTO(Integer progressId, Integer reportId, String projectId,
                              String prevWeekProg, String thisWeekProg, LocalDateTime createdAt) {
        this.progressId = progressId;
        this.reportId = reportId;
        this.projectId = projectId;
        this.prevWeekProg = prevWeekProg;
        this.thisWeekProg = thisWeekProg;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Integer getProgressId() {
        return progressId;
    }

    public void setProgressId(Integer progressId) {
        this.progressId = progressId;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPrevWeekProg() {
        return prevWeekProg;
    }

    public void setPrevWeekProg(String prevWeekProg) {
        this.prevWeekProg = prevWeekProg;
    }

    public String getThisWeekProg() {
        return thisWeekProg;
    }

    public void setThisWeekProg(String thisWeekProg) {
        this.thisWeekProg = thisWeekProg;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Convert Entity to DTO
    public static ProjectProgressDTO fromEntity(ProjectProgress progress) {
        ProjectProgressDTO dto = new ProjectProgressDTO();
        dto.setProgressId(progress.getProgressId());
        dto.setReportId(progress.getReportId());
        dto.setProjectId(progress.getProjectId());
        dto.setPrevWeekProg(progress.getPrevWeekProg());
        dto.setThisWeekProg(progress.getThisWeekProg());
        dto.setCreatedAt(progress.getCreatedAt());
        return dto;
    }

    // Convert DTO to Entity
    public ProjectProgress toEntity() {
        ProjectProgress entity = new ProjectProgress();
        if (this.progressId != null) {
            entity.setProgressId(this.progressId);
        }
        entity.setReportId(this.reportId);
        entity.setProjectId(this.projectId);
        entity.setPrevWeekProg(this.prevWeekProg);
        entity.setThisWeekProg(this.thisWeekProg);
        // createdAt is managed by entity lifecycle events
        return entity;
    }

    @Override
    public String toString() {
        return "ProjectProgressDTO{" +
                "progressId=" + progressId +
                ", reportId=" + reportId +
                ", projectId='" + projectId + '\'' +
                ", prevWeekProg='" + prevWeekProg + '\'' +
                ", thisWeekProg='" + thisWeekProg + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}