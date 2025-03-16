package com.backend.nsl_workspace.dto;

import com.backend.nsl_workspace.entity.Report;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReportDTO {

    private Integer reportId;
    private Integer userId;
    private String monthlyGoals;
    private String annualGoals;
    private LocalDate reportDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProjectProgressDTO> projectProgresses;

    // Default constructor
    public ReportDTO() {
    }

    // Constructor with all fields
    public ReportDTO(Integer reportId, Integer userId, String monthlyGoals, String annualGoals,
                     LocalDate reportDate, LocalDateTime createdAt, LocalDateTime updatedAt,
                     List<ProjectProgressDTO> projectProgresses) {
        this.reportId = reportId;
        this.userId = userId;
        this.monthlyGoals = monthlyGoals;
        this.annualGoals = annualGoals;
        this.reportDate = reportDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.projectProgresses = projectProgresses;
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

    public List<ProjectProgressDTO> getProjectProgresses() {
        return projectProgresses;
    }

    public void setProjectProgresses(List<ProjectProgressDTO> projectProgresses) {
        this.projectProgresses = projectProgresses;
    }

    // Convert Entity to DTO
    public static ReportDTO fromEntity(Report report) {
        ReportDTO dto = new ReportDTO();
        dto.setReportId(report.getReportId());
        dto.setUserId(report.getUserId());
        dto.setMonthlyGoals(report.getMonthlyGoals());
        dto.setAnnualGoals(report.getAnnualGoals());
        dto.setReportDate(report.getReportDate());
        dto.setCreatedAt(report.getCreatedAt());
        dto.setUpdatedAt(report.getUpdatedAt());
        return dto;
    }

    // Convert DTO to Entity
    public Report toEntity() {
        Report entity = new Report();
        if (this.reportId != null) {
            entity.setReportId(this.reportId);
        }
        entity.setUserId(this.userId);
        entity.setMonthlyGoals(this.monthlyGoals);
        entity.setAnnualGoals(this.annualGoals);
        entity.setReportDate(this.reportDate);
        // createdAt and updatedAt are managed by entity lifecycle events
        return entity;
    }

    @Override
    public String toString() {
        return "ReportDTO{" +
                "reportId=" + reportId +
                ", userId=" + userId +
                ", monthlyGoals='" + monthlyGoals + '\'' +
                ", annualGoals='" + annualGoals + '\'' +
                ", reportDate=" + reportDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", projectProgresses=" + projectProgresses +
                '}';
    }
}