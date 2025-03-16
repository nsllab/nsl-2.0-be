package com.backend.nsl_workspace.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "publication_p_tbl")
@IdClass(PublicationJunctionId.class)  // Add this to use composite key
public class PublicationJunction {
    @Id
    @Column(name = "publication_id")
    private Integer publicationId;

    @Id  // Add this to make paper_id part of the composite key
    @Column(name = "paper_id")
    private Integer paperId;

    // Getters and Setters remain the same
    public Integer getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(Integer publicationId) {
        this.publicationId = publicationId;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }
}

// Add this class to represent the composite key
class PublicationJunctionId implements Serializable {
    private Integer publicationId;
    private Integer paperId;

    public PublicationJunctionId() {}

    public PublicationJunctionId(Integer publicationId, Integer paperId) {
        this.publicationId = publicationId;
        this.paperId = paperId;
    }

    // Getters and setters
    public Integer getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(Integer publicationId) {
        this.publicationId = publicationId;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicationJunctionId that = (PublicationJunctionId) o;
        return publicationId.equals(that.publicationId) &&
                paperId.equals(that.paperId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicationId, paperId);
    }
}