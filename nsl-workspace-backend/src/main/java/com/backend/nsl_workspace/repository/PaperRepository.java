package com.backend.nsl_workspace.repository;

import com.backend.nsl_workspace.entity.Paper;
import com.backend.nsl_workspace.entity.PaperAuthor;
import com.backend.nsl_workspace.enums.PaperStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaperRepository extends JpaRepository<Paper, Integer> {
    List<Paper> findByResearchAreaId(Integer researchAreaId);
    List<Paper> findByPaperStatus(PaperStatus status);
}