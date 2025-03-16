package com.backend.nsl_workspace.repository;

import com.backend.nsl_workspace.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query("SELECT MAX(CAST(SUBSTRING(CAST(p.projectId AS string), 9) AS int)) " +
            "FROM Project p WHERE CAST(p.projectId AS string) LIKE :datePrefix%")
    Integer findLatestIdForDate(@Param("datePrefix") String datePrefix);

    Optional<Project> findByProjectId(String projectId);

    /**
     * Find projects by exact name match
     * @param projectName Project name
     * @return List of matching projects
     */
    List<Project> findByProjectName(String projectName);

    /**
     * Find projects where name contains the search term
     * @param projectName Project name (partial)
     * @return List of matching projects
     */
    List<Project> findByProjectNameContaining(String projectName);

    /**
     * Find projects by research area ID
     * @param researchAreaId Research area ID
     * @return List of matching projects
     */
    List<Project> findByResearchAreaId(Integer researchAreaId);
}