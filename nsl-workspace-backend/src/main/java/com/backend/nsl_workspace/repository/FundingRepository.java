package com.backend.nsl_workspace.repository;

import com.backend.nsl_workspace.entity.Funding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundingRepository extends JpaRepository<Funding, Integer> {
}