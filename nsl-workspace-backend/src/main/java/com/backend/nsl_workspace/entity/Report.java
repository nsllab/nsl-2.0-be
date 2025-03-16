package com.backend.nsl_workspace.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "labwork_report_tbl")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "monthly_goals", columnDefinition = "TEXT")
    private String monthlyGoals;

    @Column(name = "annual_goals", columnDefinition = "TEXT")
    private String annualGoals;

    @Column(name = "report_date")
    private LocalDate reportDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Default constructor
    public Report() {
    }

    // Constructor with all fields
    public Report(Integer reportId, Integer userId, String monthlyGoals, String annualGoals,
                  LocalDate reportDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.reportId = reportId;
        this.userId = userId;
        this.monthlyGoals = monthlyGoals;
        this.annualGoals = annualGoals;
        this.reportDate = reportDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMonthlyGoals() {
        return monthlyGoals;
    }

    public void setMonthlyGoals(String monthlyGoals) {
        this.monthlyGoals = monthlyGoals;
    }

    public String getAnnualGoals() {
        return annualGoals;
    }

    public void setAnnualGoals(String annualGoals) {
        this.annualGoals = annualGoals;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Lifecycle methods
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Report{" +
                "reportId=" + reportId +
                ", userId=" + userId +
                ", monthlyGoals='" + monthlyGoals + '\'' +
                ", annualGoals='" + annualGoals + '\'' +
                ", reportDate=" + reportDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}