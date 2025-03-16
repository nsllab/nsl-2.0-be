package com.backend.nsl_workspace.entity;

import java.io.Serializable;
import java.util.Objects;

// Composite key class
public class PaperAuthorId implements Serializable {
    private Integer paperId;
    private Integer userId;

    // Default constructor
    public PaperAuthorId() {}

    public PaperAuthorId(Integer paperId, Integer userId) {
        this.paperId = paperId;
        this.userId = userId;
    }

    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaperAuthorId that = (PaperAuthorId) o;
        return Objects.equals(paperId, that.paperId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paperId, userId);
    }
}
