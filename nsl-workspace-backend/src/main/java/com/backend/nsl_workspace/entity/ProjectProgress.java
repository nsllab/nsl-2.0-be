package com.backend.nsl_workspace.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "labwork_project_progress_tbl")
public class ProjectProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Integer progressId;

    @Column(name = "report_id", nullable = false)
    private Integer reportId;

    @Column(name = "project_id", nullable = false, length = 20)
    private String projectId;

    @Column(name = "prev_week_prog", columnDefinition = "TEXT")
    private String prevWeekProg;

    @Column(name = "this_week_prog", columnDefinition = "TEXT")
    private String thisWeekProg;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Default constructor
    public ProjectProgress() {
    }

    // Constructor with all fields
    public ProjectProgress(Integer progressId, Integer reportId, String projectId,
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

    // Lifecycle methods
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "ProjectProgress{" +
                "progressId=" + progressId +
                ", reportId=" + reportId +
                ", projectId='" + projectId + '\'' +
                ", prevWeekProg='" + prevWeekProg + '\'' +
                ", thisWeekProg='" + thisWeekProg + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}