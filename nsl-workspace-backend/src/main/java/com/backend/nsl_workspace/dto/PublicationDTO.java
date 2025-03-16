package com.backend.nsl_workspace.dto;

import com.backend.nsl_workspace.enums.PublicationType;
import com.backend.nsl_workspace.enums.PublicationClass;

public class PublicationDTO {
    private Integer id;
    private String publicationName;
    private PublicationType publicationType;
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