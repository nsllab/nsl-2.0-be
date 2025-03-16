package com.backend.nsl_workspace.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "funding_tbl")
public class Funding {
    @Id
    @Column(name = "funding_id")
    private Integer fundingId;

    @Column(name = "funding_name", nullable = false)
    private String fundingName;

    @Column(name = "funding_desc")
    private String fundingDesc;

    @Column(name = "funding_source")
    private String fundingSource;

    // Getters and Setters
    public Integer getFundingId() {
        return fundingId;
    }

    public void setFundingId(Integer fundingId) {
        this.fundingId = fundingId;
    }

    public String getFundingName() {
        return fundingName;
    }

    public void setFundingName(String fundingName) {
        this.fundingName = fundingName;
    }

    public String getFundingDesc() {
        return fundingDesc;
    }

    public void setFundingDesc(String fundingDesc) {
        this.fundingDesc = fundingDesc;
    }

    public String getFundingSource() {
        return fundingSource;
    }

    public void setFundingSource(String fundingSource) {
        this.fundingSource = fundingSource;
    }
}