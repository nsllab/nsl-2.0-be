package com.backend.nsl_workspace.controller;

import com.backend.nsl_workspace.dto.PaperDTO;
import com.backend.nsl_workspace.dto.UserDTO;
import com.backend.nsl_workspace.entity.PaperAuthor;
import com.backend.nsl_workspace.entity.PublicationJunction;
import com.backend.nsl_workspace.enums.PaperStatus;
import com.backend.nsl_workspace.service.JwtService;
import com.backend.nsl_workspace.service.PaperAuthorService;
import com.backend.nsl_workspace.service.PaperService;
import com.backend.nsl_workspace.service.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/papers")
public class PaperController {

    @Autowired
    private PaperService paperService;

    @Autowired
    private PaperAuthorService paperAuthorService;

    @Autowired
    private PublicationService publicationService;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity<List<PaperDTO>> getAllPapers() {
        List<PaperDTO> papers = paperService.getAllPapers();
        return ResponseEntity.ok(papers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaperById(@PathVariable Integer id) {
        return paperService.getPaperById(id);
    }

    @GetMapping("/my-papers")
    public ResponseEntity<?> getMyPapers(@RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from JWT token
            String jwtToken = token.replace("Bearer ", "");
            Integer userId = jwtService.extractUserId(jwtToken); // You'll need to implement this

            // Fetch papers where the user is an author
            List<PaperDTO> userPapers = paperService.getPapersByUserId(userId);

            return ResponseEntity.ok(userPapers);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to fetch papers: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPaper(@RequestBody Map<String, Object> payload) {
        try {
            // MAIN PAPER
            String paperTitle = (String) payload.get("paperTitle");
            Integer researchAreaId = (Integer) payload.get("researchAreaId");
            String paperAbs = (String) payload.get("paperAbs");
            String paperFile = (String) payload.get("paperFile");
            String paperStatus = (String) payload.get("paperStatus");

            // PAPER AUTHOR: //PUBLICATION JUNCTION (PAPER ID)
            Integer userId = (Integer) payload.get("userId");
            Integer authorNum = (Integer) payload.get("authorNum");

            Integer publishId = (Integer) payload.get("publishId");

            // Validate all required fields
            Map<String, String> validationErrors = new HashMap<>();

            if (paperTitle == null || paperTitle.trim().isEmpty()) {
                validationErrors.put("paperTitle", "Paper title is required");
            } else if (paperTitle.length() > 255) {
                validationErrors.put("paperTitle", "Paper title must not exceed 255 characters");
            }

            if (researchAreaId == null) {
                validationErrors.put("researchAreaId", "Research area ID is required");
            } else {
                // Validate research area exists
                if (!paperService.researchAreaExists(researchAreaId)) {
                    validationErrors.put("researchAreaId", "Research area with ID " + researchAreaId + " does not exist");
                }
            }

            if (paperAbs == null || paperAbs.trim().isEmpty()) {
                validationErrors.put("paperAbs", "Paper abstract is required");
            }

            if (paperFile == null || paperFile.trim().isEmpty()) {
                validationErrors.put("paperFile", "Paper file is required");
            } else if (paperFile.length() > 255) {
                validationErrors.put("paperFile", "Paper file path must not exceed 255 characters");
            }

            // Validate dates if provided
            if (payload.get("paperSubmitDate") != null) {
                try {
                    LocalDate.parse((String) payload.get("paperSubmitDate"));
                } catch (DateTimeParseException e) {
                    validationErrors.put("paperSubmitDate", "Invalid submit date format. Use YYYY-MM-DD");
                }
            }

            if (payload.get("paperPubDate") != null) {
                try {
                    LocalDate.parse((String) payload.get("paperPubDate"));
                } catch (DateTimeParseException e) {
                    validationErrors.put("paperPubDate", "Invalid publication date format. Use YYYY-MM-DD");
                }
            }

            // Validate authors data
            List<Map<String, Object>> authorsData = (List<Map<String, Object>>) payload.get("authors");
            if (authorsData == null || authorsData.isEmpty()) {
                validationErrors.put("authors", "At least one author is required");
            } else {
                // Validate each author
                for (int i = 0; i < authorsData.size(); i++) {
                    Map<String, Object> authorData = authorsData.get(i);
                    Integer authorUserId = (Integer) authorData.get("userId");
                    Integer authorNumber = (Integer) authorData.get("authorNum");

                    // Check if user ID is provided
                    if (authorUserId == null) {
                        validationErrors.put("authors[" + i + "].userId", "User ID is required for all authors");
                        continue;
                    }

                    // Check if user exists in the database
                    if (!paperService.userExists(authorUserId)) {
                        validationErrors.put("authors[" + i + "].userId",
                                "User with ID " + authorUserId + " does not exist");
                    }

                    // Check if author number is provided
                    if (authorNumber == null) {
                        validationErrors.put("authors[" + i + "].authorNum", "Author number is required");
                    }
                }
            }

            // Validate publication ID
            if (publishId != null && !publicationService.publicationExists(publishId)) {
                validationErrors.put("publishId", "Publication with ID " + publishId + " does not exist");
            }

            // If there are validation errors, return them
            if (!validationErrors.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Validation failed");
                response.put("errors", validationErrors);
                return ResponseEntity.badRequest().body(response);
            }

            // First save the paper to get the ID
            PaperDTO paperDTO = new PaperDTO();
            paperDTO.setPaperTitle(paperTitle.trim());
            paperDTO.setResearchAreaId(researchAreaId);
            paperDTO.setPaperAbs(paperAbs.trim());
            paperDTO.setPaperFile(paperFile.trim());

            // Set optional fields if provided
            if (payload.get("paperPubDate") != null) {
                paperDTO.setPaperPubDate(LocalDate.parse((String) payload.get("paperPubDate")));
            }
            if (payload.get("paperSubmitDate") != null) {
                paperDTO.setPaperSubmitDate(LocalDate.parse((String) payload.get("paperSubmitDate")));
            }
            if (paperStatus != null) {
                try {
                    // Convert input string to match enum format: uppercase and replace spaces with underscores
                    String enumValue = paperStatus.toUpperCase().replace(" ", "_");
                    PaperStatus status = PaperStatus.valueOf(enumValue);
                    paperDTO.setPaperStatus(status);
                    // Logging
                    System.out.println("Received status: " + paperStatus);
                    System.out.println("Converted status: " + status);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid paper status: " + paperStatus);
                }
            }

            // Save paper first to get the ID
            PaperDTO savedPaper = paperService.createPaper(paperDTO);
            Integer generatedPaperId = savedPaper.getPaperId(); // Get the generated ID

            try {
                // Now use the generated ID for paper author
                List<PaperAuthor> savedAuthors = new java.util.ArrayList<>();
                for (Map<String, Object> authorData : authorsData) {
                    PaperAuthor paperAuthor = new PaperAuthor();
                    paperAuthor.setPaperId(generatedPaperId);

                    // Get the values and log them before setting
                    userId = (Integer) authorData.get("userId");
                    authorNum = (Integer) authorData.get("authorNum");

                    System.out.println("About to save: paperId=" + generatedPaperId +
                            ", userId=" + userId + ", authorNum=" + authorNum);

                    paperAuthor.setUserId(userId);
                    paperAuthor.setAuthorNum(authorNum);

                    // Save each author
                    PaperAuthor savedAuthor = paperAuthorService.createPaperAuthor(paperAuthor);
                    savedAuthors.add(savedAuthor);
                }

                // Use the generated ID for publication if publishId is provided
                PublicationJunction savedPublication = null;
                if (publishId != null) {
                    PublicationJunction junction = new PublicationJunction();
                    junction.setPublicationId(publishId);
                    junction.setPaperId(generatedPaperId);

                    // Save the related entities
                    savedPublication = publicationService.createPublicationJunct(junction);
                }

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Paper created successfully");
                response.put("paper", savedPaper);
                response.put("paper_author", savedAuthors);
                if (savedPublication != null) {
                    response.put("publish_junction", savedPublication);
                }

                return ResponseEntity.ok(response);

            } catch (Exception e) {
                // If something goes wrong after paper creation, clean up the paper record
                paperService.deletePaper(generatedPaperId);
                throw e; // Re-throw to be caught by the outer catch block
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false,
                            "message", "Paper creation failed: " + e.getMessage()));
        }
    }

    // Similar validation should be applied to the updatePaper method
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePaper(@PathVariable Integer id,
                                         @RequestBody Map<String, Object> payload) {
        // Same validation logic as createPaper
        try {
            // [Previous validation code from createPaper]

            PaperDTO paperDTO = new PaperDTO();
            // [Previous DTO population code from createPaper]

            PaperDTO updatedPaper = paperService.updatePaper(id, paperDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Paper updated successfully");
            response.put("paper", updatedPaper);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false,
                            "message", "Paper update failed: " + e.getMessage()));
        }
    }

    @PostMapping("/{paperId}/upload")
    public ResponseEntity<?> uploadPaperFile(@PathVariable Integer paperId,
                                             @RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = "context/profile_images/"; // Using your existing directory
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Create filename with paper ID prefix
            String filename = paperId + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Uploaded Successfully",
                    "fileName", filename));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to upload file: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaper(@PathVariable Integer id) {
        try {
            // Check if paper exists
            if (!paperService.paperExists(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "success", false,
                                "message", "Paper not found with id: " + id
                        ));
            }

            // Delete related records first (if they exist)
            paperAuthorService.deleteByPaperId(id);
            publicationService.deleteJunctionByPaperId(id);

            // Delete the paper
            paperService.deletePaper(id);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Paper deleted successfully"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to delete paper: " + e.getMessage()
                    ));
        }
    }

    @GetMapping("/search-users")
    public ResponseEntity<?> searchUsers(@RequestParam("query") String query) {
        try {
            List<UserDTO> users = paperService.searchUsersByQuery(query);

            if (users.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "No users found matching: " + query,
                        "users", users
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Users found",
                    "users", users
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to search users: " + e.getMessage()
                    ));
        }
    }
}