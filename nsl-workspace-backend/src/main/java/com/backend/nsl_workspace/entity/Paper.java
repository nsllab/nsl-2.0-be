package com.backend.nsl_workspace.entity;

import com.backend.nsl_workspace.enums.PaperStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "paper_tbl")
public class Paper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paper_id")
    private Integer paperId;

    @Column(name = "paper_title", length = 255)
    private String paperTitle;

    @Column(name = "research_area_id")
    private Integer researchAreaId;

    @Column(name = "paper_abs", columnDefinition = "text")
    private String paperAbs;

    @Column(name = "paper_submit_date")
    private LocalDate paperSubmitDate;

    @Column(name = "paper_status")
    @Enumerated(EnumType.STRING)
    private PaperStatus paperStatus;

    @Column(name = "paper_file", length = 255)
    private String paperFile;

    @Column(name = "paper_pub_date")
    private LocalDate paperPubDate;

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
}