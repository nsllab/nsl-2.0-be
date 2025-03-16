package com.backend.nsl_workspace.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "paper_authors")
@IdClass(PaperAuthorId.class)  // Define this class
public class PaperAuthor {
    @Id
    @Column(name = "paper_id")
    private Integer paperId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "author_num")
    private Integer authorNum;

    // Getters and Setters
    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAuthorNum() {
        return authorNum;
    }

    public void setAuthorNum(Integer authorNum) {
        this.authorNum = authorNum;
    }
}

