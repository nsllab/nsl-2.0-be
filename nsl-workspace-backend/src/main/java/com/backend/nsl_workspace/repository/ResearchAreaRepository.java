package com.backend.nsl_workspace.repository;

import com.backend.nsl_workspace.entity.ResearchArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResearchAreaRepository extends JpaRepository<ResearchArea, Integer> {
}