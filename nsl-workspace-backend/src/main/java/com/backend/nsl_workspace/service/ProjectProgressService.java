package com.backend.nsl_workspace.service;

import com.backend.nsl_workspace.dto.ProjectProgressDTO;
import com.backend.nsl_workspace.entity.ProjectProgress;
import com.backend.nsl_workspace.repository.ProjectProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectProgressService {

    @Autowired
    private ProjectProgressRepository projectProgressRepository;

    /**
     * Get all project progress entries
     * @return List of all project progress entries
     */
    public List<ProjectProgressDTO> getAllProjectProgress() {
        return projectProgressRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a project progress entry by ID
     * @param id Progress entry ID
     * @return ProjectProgressDTO or null if not found
     */
    public ProjectProgressDTO getProjectProgressById(Integer id) {
        Optional<ProjectProgress> progress = projectProgressRepository.findById(id);
        return progress.map(this::convertToDTO).orElse(null);
    }

    /**
     * Get all project progress entries for a report
     * @param reportId Report ID
     * @return List of project progress entries
     */
    public List<ProjectProgressDTO> getProgressByReportId(Integer reportId) {
        return projectProgressRepository.findByReportId(reportId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all project progress entries for a project
     * @param projectId Project ID
     * @return List of project progress entries
     */
    public List<ProjectProgressDTO> getProgressByProjectId(String projectId) {
        return projectProgressRepository.findByProjectId(projectId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get project progress entries for a specific report and project
     * @param reportId Report ID
     * @param projectId Project ID
     * @return List of project progress entries
     */
    public List<ProjectProgressDTO> getProgressByReportAndProject(Integer reportId, String projectId) {
        return projectProgressRepository.findByReportIdAndProjectId(reportId, projectId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create a new project progress entry
     * @param progressDTO Project progress DTO
     * @return Created progress entry as DTO
     */
    public ProjectProgressDTO createProjectProgress(ProjectProgressDTO progressDTO) {
        // Validate the input data
        if (progressDTO.getReportId() == null) {
            throw new IllegalArgumentException("Report ID is required");
        }
        if (progressDTO.getProjectId() == null || progressDTO.getProjectId().trim().isEmpty()) {
            throw new IllegalArgumentException("Project ID is required");
        }

        ProjectProgress progress = convertToEntity(progressDTO);
        ProjectProgress savedProgress = projectProgressRepository.save(progress);
        return convertToDTO(savedProgress);
    }

    /**
     * Create multiple project progress entries
     * @param progressDTOs List of project progress DTOs
     * @return List of created progress entries as DTOs
     */
    public List<ProjectProgressDTO> createMultipleProjectProgress(List<ProjectProgressDTO> progressDTOs) {
        // Validate all entries have required fields
        for (ProjectProgressDTO dto : progressDTOs) {
            if (dto.getReportId() == null) {
                throw new IllegalArgumentException("Report ID is required for all progress entries");
            }
            if (dto.getProjectId() == null || dto.getProjectId().trim().isEmpty()) {
                throw new IllegalArgumentException("Project ID is required for all progress entries");
            }
        }

        List<ProjectProgress> entities = progressDTOs.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());

        List<ProjectProgress> savedEntities = projectProgressRepository.saveAll(entities);

        return savedEntities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update a project progress entry
     * @param id Progress entry ID
     * @param progressDTO Project progress DTO
     * @return Updated progress entry as DTO
     */
    public ProjectProgressDTO updateProjectProgress(Integer id, ProjectProgressDTO progressDTO) {
        if (!projectProgressRepository.existsById(id)) {
            throw new RuntimeException("Project progress not found with id: " + id);
        }

        // Get existing entity to preserve created_at
        ProjectProgress existingProgress = projectProgressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project progress not found with id: " + id));

        // Update only the fields that should be updated
        if (progressDTO.getProjectId() != null && !progressDTO.getProjectId().trim().isEmpty()) {
            existingProgress.setProjectId(progressDTO.getProjectId());
        }
        if (progressDTO.getPrevWeekProg() != null) {
            existingProgress.setPrevWeekProg(progressDTO.getPrevWeekProg());
        }
        if (progressDTO.getThisWeekProg() != null) {
            existingProgress.setThisWeekProg(progressDTO.getThisWeekProg());
        }

        ProjectProgress updatedProgress = projectProgressRepository.save(existingProgress);
        return convertToDTO(updatedProgress);
    }

    /**
     * Delete a project progress entry
     * @param id Progress entry ID
     */
    public void deleteProjectProgress(Integer id) {
        if (!projectProgressRepository.existsById(id)) {
            throw new RuntimeException("Project progress not found with id: " + id);
        }
        projectProgressRepository.deleteById(id);
    }

    /**
     * Delete all project progress entries for a report
     * @param reportId Report ID
     */
    public void deleteProgressByReportId(Integer reportId) {
        projectProgressRepository.deleteByReportId(reportId);
    }

    /**
     * Convert ProjectProgress entity to ProjectProgressDTO
     * @param progress ProjectProgress entity
     * @return ProjectProgressDTO
     */
    private ProjectProgressDTO convertToDTO(ProjectProgress progress) {
        ProjectProgressDTO dto = new ProjectProgressDTO();
        dto.setProgressId(progress.getProgressId());
        dto.setReportId(progress.getReportId());
        dto.setProjectId(progress.getProjectId());
        dto.setPrevWeekProg(progress.getPrevWeekProg());
        dto.setThisWeekProg(progress.getThisWeekProg());
        dto.setCreatedAt(progress.getCreatedAt());
        return dto;
    }

    /**
     * Convert ProjectProgressDTO to ProjectProgress entity
     * @param dto ProjectProgressDTO
     * @return ProjectProgress entity
     */
    private ProjectProgress convertToEntity(ProjectProgressDTO dto) {
        ProjectProgress entity = new ProjectProgress();
        if (dto.getProgressId() != null) {
            entity.setProgressId(dto.getProgressId());
        }
        entity.setReportId(dto.getReportId());
        entity.setProjectId(dto.getProjectId());
        entity.setPrevWeekProg(dto.getPrevWeekProg());
        entity.setThisWeekProg(dto.getThisWeekProg());
        return entity;
    }
}