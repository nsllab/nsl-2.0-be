package com.backend.nsl_workspace.dto;

import com.backend.nsl_workspace.entity.Paper;
import com.backend.nsl_workspace.enums.PaperStatus;

import java.time.LocalDate;

public class PaperDTO {
    private Integer paperId;
    private String paperTitle;
    private Integer researchAreaId;
    private String paperAbs;
    private LocalDate paperSubmitDate;
    private PaperStatus paperStatus;
    private String paperFile;
    private LocalDate paperPubDate;

    // Default Constructor
    public PaperDTO() {}

    // Getters and Setters
    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public String getPaperTitle() {
        return paperTitle;
    }

    public void setPaperTitle(String paperTitle) {
        this.paperTitle = paperTitle;
    }

    public Integer getResearchAreaId() {
        return researchAreaId;
    }

    public void setResearchAreaId(Integer researchAreaId) {
        this.researchAreaId = researchAreaId;
    }

    public String getPaperAbs() {
        return paperAbs;
    }

    public void setPaperAbs(String paperAbs) {
        this.paperAbs = paperAbs;
    }

    public LocalDate getPaperSubmitDate() {
        return paperSubmitDate;
    }

    public void setPaperSubmitDate(LocalDate paperSubmitDate) {
        this.paperSubmitDate = paperSubmitDate;
    }

    public PaperStatus getPaperStatus() {
        return paperStatus;
    }

    public void setPaperStatus(PaperStatus paperStatus) {
        this.paperStatus = paperStatus;
    }

    public String getPaperFile() {
        return paperFile;
    }

    public void setPaperFile(String paperFile) {
        this.paperFile = paperFile;
    }

    public LocalDate getPaperPubDate() {
        return paperPubDate;
    }

    public void setPaperPubDate(LocalDate paperPubDate) {
        this.paperPubDate = paperPubDate;
    }

    // Convert Entity to DTO
    public static PaperDTO fromEntity(Paper paper) {
        PaperDTO dto = new PaperDTO();
        dto.setPaperId(paper.getPaperId());
        dto.setPaperTitle(paper.getPaperTitle());
        dto.setResearchAreaId(paper.getResearchAreaId());
        dto.setPaperAbs(paper.getPaperAbs());
        dto.setPaperSubmitDate(paper.getPaperSubmitDate());
        dto.setPaperStatus(paper.getPaperStatus());
        dto.setPaperFile(paper.getPaperFile());
        dto.setPaperPubDate(paper.getPaperPubDate());
        return dto;
    }

    // Convert DTO to Entity
    public Paper toEntity() {
        Paper paper = new Paper();
        paper.setPaperId(this.paperId);
        paper.setPaperTitle(this.paperTitle);
        paper.setResearchAreaId(this.researchAreaId);
        paper.setPaperAbs(this.paperAbs);
        paper.setPaperSubmitDate(this.paperSubmitDate);
        paper.setPaperStatus(this.paperStatus);
        paper.setPaperFile(this.paperFile);
        paper.setPaperPubDate(this.paperPubDate);
        return paper;
    }
}