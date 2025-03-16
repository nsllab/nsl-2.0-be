package com.backend.nsl_workspace.entity;

import com.backend.nsl_workspace.enums.PublicationType;
import com.backend.nsl_workspace.enums.PublicationClass;
import jakarta.persistence.*;

@Entity
@Table(name = "publication_tbl")
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publication_id")
    private Integer id;

    @Column(name = "publication_name")
    private String publicationName;

    @Enumerated(EnumType.STRING)
    @Column(name = "publication_type")
    private PublicationType publicationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "publication_class")
    private PublicationClass publicationClass;

    // Getters
    public Integer getId() {
        return id;
    }

    public String getPublicationName() {
        return publicationName;
    }

    public PublicationType getPublicationType() {
        return publicationType;
    }

    public PublicationClass getPublicationClass() {
        return publicationClass;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setPublicationName(String publicationName) {
        this.publicationName = publicationName;
    }

    public void setPublicationType(PublicationType publicationType) {
        this.publicationType = publicationType;
    }

    public void setPublicationClass(PublicationClass publicationClass) {
        this.publicationClass = publicationClass;
    }
}