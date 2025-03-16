package com.backend.nsl_workspace.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "research_area_tbl")
public class ResearchArea {
    @Id
    @Column(name = "research_area_id")
    private Integer researchAreaId;

    @Column(name = "research_area_name", nullable = false)
    private String researchAreaName;

    @Column(name = "research_area_desc")
    private String researchAreaDesc;

    @Column(name = "research_area_created_at", nullable = false)
    private LocalDateTime researchAreaCreatedAt;

    // Getters and Setters
    public Integer getResearchAreaId() {
        return researchAreaId;
    }

    public void setResearchAreaId(Integer researchAreaId) {
        this.researchAreaId = researchAreaId;
    }

    public String getResearchAreaName() {
        return researchAreaName;
    }

    public void setResearchAreaName(String researchAreaName) {
        this.researchAreaName = researchAreaName;
    }

    public String getResearchAreaDesc() {
        return researchAreaDesc;
    }

    public void setResearchAreaDesc(String researchAreaDesc) {
        this.researchAreaDesc = researchAreaDesc;
    }

    public LocalDateTime getResearchAreaCreatedAt() {
        return researchAreaCreatedAt;
    }

    public void setResearchAreaCreatedAt(LocalDateTime researchAreaCreatedAt) {
        this.researchAreaCreatedAt = researchAreaCreatedAt;
    }
}