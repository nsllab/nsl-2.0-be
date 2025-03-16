package com.backend.nsl_workspace.controller;

import com.backend.nsl_workspace.dto.ProjectProgressDTO;
import com.backend.nsl_workspace.service.JwtService;
import com.backend.nsl_workspace.service.ProjectProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/project-progress")
public class ProjectProgressController {

    @Autowired
    private ProjectProgressService projectProgressService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/report/{reportId}")
    public ResponseEntity<?> getProjectProgressByReportId(@PathVariable Integer reportId,
                                                          @RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from JWT token for authorization
            String jwtToken = token.replace("Bearer ", "");
            Integer userId = jwtService.extractUserId(jwtToken);

            // Fetch project progress for the report
            List<ProjectProgressDTO> progress = projectProgressService.getProgressByReportId(reportId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "progress", progress
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to fetch project progress: " + e.getMessage()
                    ));
        }
    }

    @PostMapping
    public ResponseEntity<?> createProjectProgress(@RequestBody ProjectProgressDTO progressDTO,
                                                   @RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from JWT token for authorization
            String jwtToken = token.replace("Bearer ", "");
            Integer userId = jwtService.extractUserId(jwtToken);

            // Create the project progress entry
            ProjectProgressDTO savedProgress = projectProgressService.createProjectProgress(progressDTO);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Project progress created successfully",
                    "progress", savedProgress
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to create project progress: " + e.getMessage()
                    ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProjectProgress(@PathVariable Integer id,
                                                   @RequestBody ProjectProgressDTO progressDTO,
                                                   @RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from JWT token for authorization
            String jwtToken = token.replace("Bearer ", "");
            Integer userId = jwtService.extractUserId(jwtToken);

            // Update the project progress entry
            ProjectProgressDTO updatedProgress = projectProgressService.updateProjectProgress(id, progressDTO);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Project progress updated successfully",
                    "progress", updatedProgress
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to update project progress: " + e.getMessage()
                    ));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProjectProgress(@PathVariable Integer id,
                                                   @RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from JWT token for authorization
            String jwtToken = token.replace("Bearer ", "");
            Integer userId = jwtService.extractUserId(jwtToken);

            // Delete the project progress entry
            projectProgressService.deleteProjectProgress(id);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Project progress deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to delete project progress: " + e.getMessage()
                    ));
        }
    }
}