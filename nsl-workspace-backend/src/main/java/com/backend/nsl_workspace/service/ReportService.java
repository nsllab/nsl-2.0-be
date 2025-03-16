package com.backend.nsl_workspace.service;

import com.backend.nsl_workspace.dto.ProjectProgressDTO;
import com.backend.nsl_workspace.dto.ReportDTO;
import com.backend.nsl_workspace.entity.Report;
import com.backend.nsl_workspace.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ProjectProgressService projectProgressService;

    /**
     * Get all reports
     * @return List of all reports as DTOs
     */
    public List<ReportDTO> getAllReports() {
        return reportRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a report by ID
     * @param id Report ID
     * @return ResponseEntity with the report or error
     */
    public ResponseEntity<?> getReportById(Integer id) {
        Optional<Report> report = reportRepository.findById(id);
        if(report.isPresent()) {
            ReportDTO reportDTO = convertToDTO(report.get());

            // Fetch associated project progress entries
            List<ProjectProgressDTO> progressEntries = projectProgressService.getProgressByReportId(id);
            reportDTO.setProjectProgresses(progressEntries);

            return ResponseEntity.ok(reportDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Report not found with id: " + id));
    }

    /**
     * Get reports by user ID
     * @param userId User ID
     * @return List of reports for the user
     */
    public List<ReportDTO> getReportsByUserId(Integer userId) {
        List<Report> reports = reportRepository.findByUserId(userId);

        return reports.stream()
                .map(report -> {
                    ReportDTO dto = convertToDTO(report);

                    // Fetch associated project progress entries for each report
                    List<ProjectProgressDTO> progressEntries =
                            projectProgressService.getProgressByReportId(report.getReportId());
                    dto.setProjectProgresses(progressEntries);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get reports by user ID and date range
     * @param userId User ID
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return List of reports in the date range
     */
    public List<ReportDTO> getReportsByUserIdAndDateRange(Integer userId,
                                                          LocalDate startDate,
                                                          LocalDate endDate) {
        List<Report> reports = reportRepository.findByUserIdAndReportDateBetween(userId, startDate, endDate);

        return reports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get the most recent report for a user
     * @param userId User ID
     * @return The most recent report or null
     */
    public ReportDTO getMostRecentReport(Integer userId) {
        List<Report> reports = reportRepository.findByUserIdOrderByReportDateDesc(userId);

        if (reports.isEmpty()) {
            return null;
        }

        Report mostRecent = reports.get(0);
        ReportDTO dto = convertToDTO(mostRecent);

        // Fetch associated project progress entries
        List<ProjectProgressDTO> progressEntries =
                projectProgressService.getProgressByReportId(mostRecent.getReportId());
        dto.setProjectProgresses(progressEntries);

        return dto;
    }

    /**
     * Create a new report
     * @param reportDTO Report data transfer object
     * @return Created report as DTO
     */
    public ReportDTO createReport(ReportDTO reportDTO) {
        Report report = convertToEntity(reportDTO);
        Report savedReport = reportRepository.save(report);
        return convertToDTO(savedReport);
    }

    /**
     * Update a report
     * @param id Report ID
     * @param reportDTO Report data transfer object
     * @return Updated report as DTO
     */
    public ReportDTO updateReport(Integer id, ReportDTO reportDTO) {
        if (!reportRepository.existsById(id)) {
            throw new RuntimeException("Report not found with id: " + id);
        }

        // Get the existing report so we don't lose the user ID
        Report existingReport = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + id));

        // Update only the fields that are provided
        if (reportDTO.getMonthlyGoals() != null) {
            existingReport.setMonthlyGoals(reportDTO.getMonthlyGoals());
        }
        if (reportDTO.getAnnualGoals() != null) {
            existingReport.setAnnualGoals(reportDTO.getAnnualGoals());
        }
        if (reportDTO.getReportDate() != null) {
            existingReport.setReportDate(reportDTO.getReportDate());
        }

        Report updatedReport = reportRepository.save(existingReport);
        return convertToDTO(updatedReport);
    }

    /**
     * Check if a report exists
     * @param id Report ID
     * @return true if exists, false otherwise
     */
    public boolean reportExists(Integer id) {
        return reportRepository.existsById(id);
    }

    /**
     * Delete a report
     * @param id Report ID
     */
    public void deleteReport(Integer id) {
        reportRepository.deleteById(id);
    }

    /**
     * Convert Report entity to ReportDTO
     * @param report Report entity
     * @return Report DTO
     */
    private ReportDTO convertToDTO(Report report) {
        ReportDTO dto = new ReportDTO();
        dto.setReportId(report.getReportId());
        dto.setUserId(report.getUserId());
        dto.setMonthlyGoals(report.getMonthlyGoals());
        dto.setAnnualGoals(report.getAnnualGoals());
        dto.setReportDate(report.getReportDate());
        dto.setCreatedAt(report.getCreatedAt());
        dto.setUpdatedAt(report.getUpdatedAt());
        return dto;
    }

    /**
     * Convert ReportDTO to Report entity
     * @param dto Report DTO
     * @return Report entity
     */
    private Report convertToEntity(ReportDTO dto) {
        Report entity = new Report();
        if (dto.getReportId() != null) {
            entity.setReportId(dto.getReportId());
        }
        entity.setUserId(dto.getUserId());
        entity.setMonthlyGoals(dto.getMonthlyGoals());
        entity.setAnnualGoals(dto.getAnnualGoals());
        entity.setReportDate(dto.getReportDate() != null ? dto.getReportDate() : LocalDate.now());
        return entity;
    }
}