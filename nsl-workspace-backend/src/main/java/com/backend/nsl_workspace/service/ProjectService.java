package com.backend.nsl_workspace.service;

import com.backend.nsl_workspace.dto.ProjectDTO;
import com.backend.nsl_workspace.entity.Project;
import com.backend.nsl_workspace.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public String generateProjectId(int sequentialNumber) {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "PRJ-" + datePart + "-" + String.format("%04d", sequentialNumber);
    }

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        try {
            Project project = new Project();

            // Generate project_id (YYYYMMDDNN format)
            LocalDate today = LocalDate.now();
            String datePrefix = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            Integer latestId = projectRepository.findLatestIdForDate(datePrefix);
            String projectFormat = "PRJ-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "-";
            int nextSequence = (latestId != null) ? latestId + 1 : 1;

            // Generate the project ID in the format: PRJ-YYYYMMDD-XXXX
            String projectId = String.format("%s%04d", projectFormat, nextSequence);

            // Set the project ID as a string
            project.setProjectId(projectId);

            project.setProjectName(projectDTO.getProjectName());
            project.setProjectDesc(projectDTO.getProjectDesc());
            project.setResearchAreaId(projectDTO.getResearchAreaId());
            project.setProjImgPath(projectDTO.getProjImgPath());
            project.setStartDate(projectDTO.getStartDate());
            project.setEndDate(projectDTO.getEndDate());
            project.setFundingId(projectDTO.getFundingId());
            project.setCreatedAt(LocalDateTime.now());
            project.setUpdatedAt(LocalDateTime.now());

            Project savedProject = projectRepository.save(project);

            ProjectDTO savedDTO = new ProjectDTO();
            convertToDTO(savedProject, savedDTO);
            return savedDTO;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create project: " + e.getMessage());
        }
    }

    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(project -> {
                    ProjectDTO dto = new ProjectDTO();
                    convertToDTO(project, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get all project entities (not DTOs)
     * @return List of all project entities
     */
    public List<Project> getAllProjectEntities() {
        return projectRepository.findAll();
    }

    private void convertToDTO(Project project, ProjectDTO dto) {
        dto.setProjectId(project.getProjectId());
        dto.setProjectName(project.getProjectName());
        dto.setProjectDesc(project.getProjectDesc());
        dto.setResearchAreaId(project.getResearchAreaId());
        dto.setProjImgPath(project.getProjImgPath());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setFundingId(project.getFundingId());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());
    }

    public void deleteProject(Integer id) {
        projectRepository.deleteById(id);
    }

    /**
     * Find project by project ID
     * @param projectId Project ID
     * @return Optional containing project if found
     */
    public Optional<Project> findByProjectId(String projectId) {
        return projectRepository.findByProjectId(projectId);
    }

    /**
     * Find projects by exact name match
     * @param projectName Project name
     * @return List of matching projects
     */
    public List<Project> findProjectsByName(String projectName) {
        return projectRepository.findByProjectName(projectName);
    }

    /**
     * Find projects where name contains the search term
     * @param projectName Project name (partial)
     * @return List of matching projects
     */
    public List<Project> findProjectsByNameContaining(String projectName) {
        return projectRepository.findByProjectNameContaining(projectName);
    }

    /**
     * Find projects by research area
     * @param researchAreaId Research area ID
     * @return List of matching projects
     */
    public List<Project> findProjectsByResearchArea(Integer researchAreaId) {
        return projectRepository.findByResearchAreaId(researchAreaId);
    }

    /**
     * Get project ID by project name
     * If multiple projects have the same name, returns the first one found
     * @param projectName Project name
     * @return Project ID or null if not found
     */
    public String getProjectIdByName(String projectName) {
        List<Project> projects = projectRepository.findByProjectName(projectName);
        if (projects != null && !projects.isEmpty()) {
            return projects.get(0).getProjectId();
        }
        return null;
    }

    /**
     * Get project ID by approximate project name
     * Tries to find the best match for the given project name
     * @param projectName Project name
     * @return Project ID or null if no match found
     */
    public String findBestMatchProjectId(String projectName) {
        if (projectName == null || projectName.trim().isEmpty()) {
            return null;
        }

        // Try exact match first
        List<Project> exactMatches = projectRepository.findByProjectName(projectName.trim());
        if (exactMatches != null && !exactMatches.isEmpty()) {
            return exactMatches.get(0).getProjectId();
        }

        // Try contains match
        List<Project> containsMatches = projectRepository.findByProjectNameContaining(projectName.trim());
        if (containsMatches != null && !containsMatches.isEmpty()) {
            return containsMatches.get(0).getProjectId();
        }

        // No match found
        return null;
    }
}