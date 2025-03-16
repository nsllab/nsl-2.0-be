package com.backend.nsl_workspace.service;

import com.backend.nsl_workspace.dto.PublicationDTO;
import com.backend.nsl_workspace.entity.Publication;
import com.backend.nsl_workspace.entity.PublicationJunction;
import com.backend.nsl_workspace.repository.PublicationJunctionRepository;
import com.backend.nsl_workspace.repository.PublicationRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PublicationService {

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private PublicationJunctionRepository publicationJunctionRepository;

    // Create new publication
    public PublicationDTO createPublication(PublicationDTO publicationDTO) {
        Publication publication = new Publication();
        // Explicitly set each property
        publication.setPublicationName(publicationDTO.getPublicationName());
        publication.setPublicationType(publicationDTO.getPublicationType());
        publication.setPublicationClass(publicationDTO.getPublicationClass());

        Publication savedPublication = publicationRepository.save(publication);

        // Convert back to DTO
        PublicationDTO savedDTO = new PublicationDTO();
        savedDTO.setId(savedPublication.getId());
        savedDTO.setPublicationName(savedPublication.getPublicationName());
        savedDTO.setPublicationType(savedPublication.getPublicationType());
        savedDTO.setPublicationClass(savedPublication.getPublicationClass());

        return savedDTO;
    }

    // Get all publications
    public List<PublicationDTO> getAllPublications() {
        List<Publication> publications = publicationRepository.findAll();
        return publications.stream()
                .map(publication -> {
                    PublicationDTO dto = new PublicationDTO();
                    dto.setId(publication.getId());
                    dto.setPublicationName(publication.getPublicationName());
                    dto.setPublicationType(publication.getPublicationType());
                    dto.setPublicationClass(publication.getPublicationClass());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    public PublicationJunction createPublicationJunct(PublicationJunction publication) {
        return publicationJunctionRepository.save(publication);
    }

    public Optional<PublicationJunction> getPublicationByPaperId(Integer paperId) {
        return publicationJunctionRepository.findByPaperId(paperId);
    }

    public void deleteJunctionByPaperId(Integer paperId) {
        publicationJunctionRepository.deleteByPaperId(paperId);
    }

    /**
     * Check if a publication exists in the database
     * @param publicationId The publication ID to check
     * @return true if the publication exists, false otherwise
     */
    public boolean publicationExists(Integer publicationId) {
        if (publicationId == null) {
            return false;
        }
        return publicationRepository.existsById(publicationId);
    }
}