package com.backend.nsl_workspace.controller;

import com.backend.nsl_workspace.dto.PublicationDTO;
import com.backend.nsl_workspace.entity.Publication;
import com.backend.nsl_workspace.enums.PublicationClass;
import com.backend.nsl_workspace.enums.PublicationType;
import com.backend.nsl_workspace.service.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/publications")
@CrossOrigin(origins = "*")
public class PublicationController {

    @Autowired
    private PublicationService publicationService;

    @PostMapping
    public ResponseEntity<?> createPublication(@RequestBody Map<String, Object> payload) {
        try {
            // Required fields validation
            String publicationName = (String) payload.get("publicationName");
            String publicationType = (String) payload.get("publicationType");
            String publicationClass = (String) payload.get("publicationClass");

            // Validate required fields
            if (publicationName == null || publicationName.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Publication name is required"));
            }
            if (publicationType == null || publicationType.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Publication type is required"));
            }
            if (publicationClass == null || publicationClass.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Publication class is required"));
            }

            PublicationDTO publicationDTO = new PublicationDTO();
            publicationDTO.setPublicationName(publicationName.trim());

            String formattedType = publicationType.substring(0, 1).toUpperCase() + publicationType.substring(1).toLowerCase();
            publicationDTO.setPublicationType(PublicationType.valueOf(formattedType));

            String formattedClass = publicationClass.substring(0, 1).toUpperCase() + publicationClass.substring(1).toLowerCase();
            publicationDTO.setPublicationClass(PublicationClass.valueOf(formattedClass));

            PublicationDTO savedPublication = publicationService.createPublication(publicationDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Publication created successfully");
            response.put("publication", savedPublication);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Invalid publication type or class: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Publication creation failed: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPublications() {
        try {
            List<PublicationDTO> publications = publicationService.getAllPublications();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("publications", publications);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Failed to fetch publications: " + e.getMessage()));
        }
    }
}