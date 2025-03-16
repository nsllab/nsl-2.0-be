package com.backend.nsl_workspace.service;

import com.backend.nsl_workspace.dto.PaperDTO;
import com.backend.nsl_workspace.dto.UserDTO;
import com.backend.nsl_workspace.entity.Paper;
import com.backend.nsl_workspace.entity.PaperAuthor;
import com.backend.nsl_workspace.entity.User;
import com.backend.nsl_workspace.enums.PaperStatus;
import com.backend.nsl_workspace.repository.PaperAuthorRepository;
import com.backend.nsl_workspace.repository.PaperRepository;
import com.backend.nsl_workspace.repository.ResearchAreaRepository;
import com.backend.nsl_workspace.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaperService {
    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private PaperAuthorRepository paperAuthorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResearchAreaRepository researchAreaRepository;

    // Fetch all papers
    public List<PaperDTO> getAllPapers() {
        return paperRepository.findAll()
                .stream()
                .map(PaperDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PaperDTO> getPapersByUserId(Integer userId) {
        // Get paper IDs where user is an author
        List<Integer> paperIds = paperAuthorRepository.findByUserId(userId)
                .stream()
                .map(PaperAuthor::getPaperId)
                .collect(Collectors.toList());

        // Get paper details for these IDs
        return paperRepository.findAllById(paperIds)
                .stream()
                .map(PaperDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Fetch paper by ID
    public ResponseEntity<?> getPaperById(Integer id) {
        Optional<Paper> paper = paperRepository.findById(id);
        if(paper.isPresent()) {
            return ResponseEntity.ok(PaperDTO.fromEntity(paper.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Paper not found with id: " + id));
    }

    // Create new paper
    public PaperDTO createPaper(PaperDTO paperDTO) {
        Paper paper = paperDTO.toEntity();
        Paper savedPaper = paperRepository.save(paper);
        return PaperDTO.fromEntity(savedPaper);
    }

    // Update paper
    public PaperDTO updatePaper(Integer id, PaperDTO paperDTO) {
        if (!paperRepository.existsById(id)) {
            throw new RuntimeException("Paper not found with id: " + id);
        }
        Paper paper = paperDTO.toEntity();
        paper.setPaperId(id);
        Paper updatedPaper = paperRepository.save(paper);
        return PaperDTO.fromEntity(updatedPaper);
    }

    public boolean paperExists(Integer id) {
        return paperRepository.existsById(id);
    }

    public void deletePaper(Integer id) {
        paperRepository.deleteById(id);
    }

    public List<UserDTO> searchUsersByQuery(String query) {
        // Remove the @ symbol if it's present at the beginning
        if (query.startsWith("@")) {
            query = query.substring(1);
        }

        // Find users that match the query directly from user_tbl
        List<User> users = userRepository.findUsersByNameLike(query);

        // Convert entities to DTOs
        return users.stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Check if a user exists in the database
     * @param userId The user ID to check
     * @return true if the user exists, false otherwise
     */
    public boolean userExists(Integer userId) {
        if (userId == null) {
            return false;
        }
        return userRepository.existsById(userId);
    }

    /**
     * Check if a research area exists in the database
     * @param researchAreaId The research area ID to check
     * @return true if the research area exists, false otherwise
     */
    public boolean researchAreaExists(Integer researchAreaId) {
        if (researchAreaId == null) {
            return false;
        }
        return researchAreaRepository.existsById(researchAreaId);
    }
}