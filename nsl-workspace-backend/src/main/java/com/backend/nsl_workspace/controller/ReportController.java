package com.backend.nsl_workspace.controller;

import com.backend.nsl_workspace.dto.ProjectDTO;
import com.backend.nsl_workspace.dto.ProjectProgressDTO;
import com.backend.nsl_workspace.dto.ReportDTO;
import com.backend.nsl_workspace.entity.Project;
import com.backend.nsl_workspace.service.JwtService;
import com.backend.nsl_workspace.service.ProjectProgressService;
import com.backend.nsl_workspace.service.ProjectService;
import com.backend.nsl_workspace.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ProjectProgressService projectProgressService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private JwtService jwtService;

    /**
     * Get all reports for the current user
     * @param token JWT token for authentication
     * @return List of reports for the user
     */
    @GetMapping("/my-reports")
    public ResponseEntity<?> getMyReports(@RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from JWT token
            String jwtToken = token.replace("Bearer ", "");
            Integer userId = jwtService.extractUserId(jwtToken);

            List<ReportDTO> reports = reportService.getReportsByUserId(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("reports", reports);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to fetch reports: " + e.getMessage()
                    ));
        }
    }

    /**
     * Get a specific report by ID
     * @param id Report ID
     * @param token JWT token for authentication
     * @return The report or error message
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getReport(@PathVariable Integer id,
                                       @RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from JWT token for authorization
            String jwtToken = token.replace("Bearer ", "");
            Integer userId = jwtService.extractUserId(jwtToken);

            // Get the report
            ResponseEntity<?> response = reportService.getReportById(id);
            if (response.getStatusCode() != HttpStatus.OK) {
                return response;
            }

            ReportDTO report = (ReportDTO) response.getBody();

            // Check if the report belongs to the authenticated user
            if (!report.getUserId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of(
                                "success", false,
                                "message", "You don't have permission to access this report"
                        ));
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "report", report
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to fetch report: " + e.getMessage()
                    ));
        }
    }

    /**
     * Get the most recent report for the current user
     * @param token JWT token for authentication
     * @return The most recent report or error message
     */
    @GetMapping("/recent")
    public ResponseEntity<?> getRecentReport(@RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from JWT token
            String jwtToken = token.replace("Bearer ", "");
            Integer userId = jwtService.extractUserId(jwtToken);

            // Get the most recent report for the user
            ReportDTO report = reportService.getMostRecentReport(userId);

            if (report == null) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "No reports found for the user",
                        "report", null
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "report", report
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to fetch recent report: " + e.getMessage()
                    ));
        }
    }

    /**
     * Create a new report
     * @param payload Request payload with report data
     * @param token JWT token for authentication
     * @return Created report
     */
    @PostMapping
    public ResponseEntity<?> createReport(@RequestBody Map<String, Object> payload,
                                          @RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from JWT token
            String jwtToken = token.replace("Bearer ", "");
            Integer userId = jwtService.extractUserId(jwtToken);

            // Extract main report details
            String monthlyGoals = (String) payload.get("monthlyGoals");
            String annualGoals = (String) payload.get("annualGoals");

            // Optional report date, default to current date if not provided
            LocalDate reportDate = payload.get("reportDate") != null
                    ? LocalDate.parse((String) payload.get("reportDate"))
                    : LocalDate.now();

            // Validate required fields
            Map<String, String> validationErrors = new HashMap<>();

            if (userId == null) {
                validationErrors.put("userId", "User ID is required");
            }

            // If there are validation errors, return them
            if (!validationErrors.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Validation failed");
                response.put("errors", validationErrors);
                return ResponseEntity.badRequest().body(response);
            }

            // Create and save the report
            ReportDTO reportDTO = new ReportDTO();
            reportDTO.setUserId(userId);
            reportDTO.setMonthlyGoals(monthlyGoals);
            reportDTO.setAnnualGoals(annualGoals);
            reportDTO.setReportDate(reportDate);

            ReportDTO savedReport = reportService.createReport(reportDTO);

            // Extract and save project progress data if available
            List<Map<String, Object>> projectsData = (List<Map<String, Object>>) payload.get("projects");
            List<ProjectProgressDTO> savedProgressEntries = new ArrayList<>();

            if (projectsData != null && !projectsData.isEmpty()) {
                List<ProjectProgressDTO> progressDTOs = new ArrayList<>();

                for (Map<String, Object> projectData : projectsData) {
                    ProjectProgressDTO progressDTO = new ProjectProgressDTO();
                    progressDTO.setReportId(savedReport.getReportId());

                    // Get project name from the request
                    String projectName = (String) projectData.get("name");

                    // Look up project ID based on project name using your existing service method
                    String projectId = null;
                    if (projectName != null && !projectName.trim().isEmpty()) {
                        projectId = projectService.findBestMatchProjectId(projectName.trim());

                        if (projectId == null) {
                            // If no existing project was found, return an error message
                            validationErrors.put("projectName", "Project '" + projectName + "' does not exist in the database. Please create the project first.");
                            continue;
                        }
                    } else {
                        validationErrors.put("projectName", "Project name is required");
                        continue;
                    }

                    progressDTO.setProjectId(projectId);
                    progressDTO.setPrevWeekProg((String) projectData.get("prevWeekProgress"));
                    progressDTO.setThisWeekProg((String) projectData.get("currentWeekPlan"));

                    progressDTOs.add(progressDTO);
                }

                // If there are validation errors, return them
                if (!validationErrors.isEmpty()) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Validation failed");
                    response.put("errors", validationErrors);
                    return ResponseEntity.badRequest().body(response);
                }

                // Save all project progress entries at once
                if (!progressDTOs.isEmpty()) {
                    savedProgressEntries = projectProgressService.createMultipleProjectProgress(progressDTOs);
                }
            }

            // Handle papers if available (not storing them but acknowledging receipt)
            List<Map<String, Object>> papersData = (List<Map<String, Object>>) payload.get("papers");
            if (papersData != null && !papersData.isEmpty()) {
                // Here you would typically process and store the paper data
                // For now, we'll just acknowledge we received it
                // TODO: Implement paper storage when the related service is available
            }

            // Add progress entries to the report
            savedReport.setProjectProgresses(savedProgressEntries);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Report created successfully");
            response.put("report", savedReport);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false,
                            "message", "Report creation failed: " + e.getMessage()));
        }
    }

    /**
     * Update a report
     * @param id Report ID
     * @param payload Request payload with report data
     * @param token JWT token for authentication
     * @return Updated report
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReport(@PathVariable Integer id,
                                          @RequestBody Map<String, Object> payload,
                                          @RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from JWT token for authorization
            String jwtToken = token.replace("Bearer ", "");
            Integer userId = jwtService.extractUserId(jwtToken);

            // Get the report to check ownership
            ResponseEntity<?> response = reportService.getReportById(id);
            if (response.getStatusCode() != HttpStatus.OK) {
                return response;
            }

            ReportDTO existingReport = (ReportDTO) response.getBody();
            if (!existingReport.getUserId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of(
                                "success", false,
                                "message", "You don't have permission to update this report"
                        ));
            }

            // Extract main report details
            String monthlyGoals = (String) payload.get("monthlyGoals");
            String annualGoals = (String) payload.get("annualGoals");

            LocalDate reportDate = payload.get("reportDate") != null
                    ? LocalDate.parse((String) payload.get("reportDate"))
                    : null;

            ReportDTO reportDTO = new ReportDTO();
            reportDTO.setMonthlyGoals(monthlyGoals);
            reportDTO.setAnnualGoals(annualGoals);
            if (reportDate != null) {
                reportDTO.setReportDate(reportDate);
            }

            ReportDTO updatedReport = reportService.updateReport(id, reportDTO);

            // Update project progress data if available
            List<Map<String, Object>> projectsData = (List<Map<String, Object>>) payload.get("projects");
            Map<String, String> validationErrors = new HashMap<>();

            if (projectsData != null && !projectsData.isEmpty()) {
                // First delete existing progress entries for this report
                projectProgressService.deleteProgressByReportId(id);

                // Then create new ones
                List<ProjectProgressDTO> progressDTOs = new ArrayList<>();

                for (Map<String, Object> projectData : projectsData) {
                    ProjectProgressDTO progressDTO = new ProjectProgressDTO();
                    progressDTO.setReportId(id);

                    // Get project name from the request
                    String projectName = (String) projectData.get("name");

                    // Look up project ID based on project name
                    String projectId = null;
                    if (projectName != null && !projectName.trim().isEmpty()) {
                        // Use the service method to find best matching project ID
                        projectId = projectService.findBestMatchProjectId(projectName.trim());

                        // If no existing project matches, create a temporary ID
                        if (projectId == null) {
                            // Create a safe ID from the name by truncating if needed
                            projectId = projectName.length() > 20
                                    ? projectName.substring(0, 20)
                                    : projectName;
                        }
                    } else {
                        validationErrors.put("projectName", "Project name is required");
                        continue; // Skip this project
                    }

                    progressDTO.setProjectId(projectId);
                    progressDTO.setPrevWeekProg((String) projectData.get("prevWeekProgress"));
                    progressDTO.setThisWeekProg((String) projectData.get("currentWeekPlan"));

                    progressDTOs.add(progressDTO);
                }

                // If there are validation errors, return them
                if (!validationErrors.isEmpty()) {
                    Map<String, Object> newResponse = new HashMap<>();
                    newResponse.put("success", false);
                    newResponse.put("message", "Validation failed");
                    newResponse.put("errors", validationErrors);
                    return ResponseEntity.badRequest().body(newResponse);
                }

                // Save all project progress entries at once
                if (!progressDTOs.isEmpty()) {
                    List<ProjectProgressDTO> savedProgressEntries =
                            projectProgressService.createMultipleProjectProgress(progressDTOs);

                    // Add progress entries to the report
                    updatedReport.setProjectProgresses(savedProgressEntries);
                }
            }

            // Handle papers if available (not storing them but acknowledging receipt)
            List<Map<String, Object>> papersData = (List<Map<String, Object>>) payload.get("papers");
            if (papersData != null && !papersData.isEmpty()) {
                // Here you would typically process and store the paper data
                // For now, we'll just acknowledge we received it
                // TODO: Implement paper storage when the related service is available
            }

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Report updated successfully");
            responseMap.put("report", updatedReport);

            return ResponseEntity.ok(responseMap);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false,
                            "message", "Report update failed: " + e.getMessage()));
        }
    }

    /**
     * Delete a report
     * @param id Report ID
     * @param token JWT token for authentication
     * @return Success or error message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable Integer id,
                                          @RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from JWT token for authorization
            String jwtToken = token.replace("Bearer ", "");
            Integer userId = jwtService.extractUserId(jwtToken);

            // Get the report to check ownership
            ResponseEntity<?> response = reportService.getReportById(id);
            if (response.getStatusCode() != HttpStatus.OK) {
                return response;
            }

            ReportDTO report = (ReportDTO) response.getBody();

            // Check if the report belongs to the authenticated user
            if (!report.getUserId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of(
                                "success", false,
                                "message", "You don't have permission to delete this report"
                        ));
            }

            // First delete all project progress entries for this report
            projectProgressService.deleteProgressByReportId(id);

            // Then delete the report
            reportService.deleteReport(id);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Report deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to delete report: " + e.getMessage()
                    ));
        }
    }

    /**
     * Get reports within a date range
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @param token JWT token for authentication
     * @return List of reports in the date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<?> getReportsByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from JWT token
            String jwtToken = token.replace("Bearer ", "");
            Integer userId = jwtService.extractUserId(jwtToken);

            // Get reports in the date range for the user
            List<ReportDTO> reports = reportService.getReportsByUserIdAndDateRange(userId, startDate, endDate);

            // For each report, fetch the project progress entries
            for (ReportDTO report : reports) {
                List<ProjectProgressDTO> progressEntries =
                        projectProgressService.getProgressByReportId(report.getReportId());
                report.setProjectProgresses(progressEntries);
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "reports", reports
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to fetch reports by date range: " + e.getMessage()
                    ));
        }
    }

    /**
     * Get available projects for reporting
     * @param token JWT token for authentication
     * @return List of available projects
     */
    @GetMapping("/projects")
    public ResponseEntity<?> getAvailableProjects(@RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from JWT token
            String jwtToken = token.replace("Bearer ", "");
            Integer userId = jwtService.extractUserId(jwtToken);

            // Fetch all projects
            List<ProjectDTO> projects = projectService.getAllProjects();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "projects", projects
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to fetch projects: " + e.getMessage()
                    ));
        }
    }

    /**
     * Search for projects by name
     * @param query Search query
     * @param token JWT token for authentication
     * @return List of matching projects
     */
    @GetMapping("/projects/search")
    public ResponseEntity<?> searchProjects(
            @RequestParam("query") String query,
            @RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from JWT token
            String jwtToken = token.replace("Bearer ", "");
            Integer userId = jwtService.extractUserId(jwtToken);

            // Search for projects by name
            List<Project> matchingProjects = projectService.findProjectsByNameContaining(query.trim());

            // Convert to DTOs
            List<ProjectDTO> projects = matchingProjects.stream()
                    .map(project -> {
                        ProjectDTO dto = new ProjectDTO();
                        dto.setProjectId(project.getProjectId());
                        dto.setProjectName(project.getProjectName());
                        dto.setProjectDesc(project.getProjectDesc());
                        dto.setResearchAreaId(project.getResearchAreaId());
                        return dto;
                    })
                    .collect(java.util.stream.Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "projects", projects
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to search projects: " + e.getMessage()
                    ));
        }
    }
}