package com.backend.nsl_workspace.repository;

import com.backend.nsl_workspace.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    /**
     * Find reports by user ID
     * @param userId The ID of the user
     * @return List of reports for the user
     */
    List<Report> findByUserId(Integer userId);

    /**
     * Find reports by user ID and date range
     * @param userId The ID of the user
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return List of reports within the date range
     */
    List<Report> findByUserIdAndReportDateBetween(Integer userId, LocalDate startDate, LocalDate endDate);

    /**
     * Find the most recent report for a user
     * @param userId The ID of the user
     * @return List of reports ordered by date (most recent first)
     */
    List<Report> findByUserIdOrderByReportDateDesc(Integer userId);
}