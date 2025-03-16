package com.backend.nsl_workspace.repository;

import com.backend.nsl_workspace.entity.ProjectProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProjectProgressRepository extends JpaRepository<ProjectProgress, Integer> {

    /**
     * Find progress entries by report ID
     * @param reportId The ID of the report
     * @return List of project progress entries
     */
    List<ProjectProgress> findByReportId(Integer reportId);

    /**
     * Find progress entries by project ID
     * @param projectId The ID of the project
     * @return List of project progress entries
     */
    List<ProjectProgress> findByProjectId(String projectId);

    /**
     * Find progress entries by report ID and project ID
     * @param reportId The ID of the report
     * @param projectId The ID of the project
     * @return List of project progress entries
     */
    List<ProjectProgress> findByReportIdAndProjectId(Integer reportId, String projectId);

    /**
     * Delete all progress entries for a report
     * @param reportId The ID of the report
     */
    @Transactional
    void deleteByReportId(Integer reportId);
}