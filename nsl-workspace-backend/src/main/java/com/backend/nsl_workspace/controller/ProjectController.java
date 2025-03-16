package com.backend.nsl_workspace.controller;

import com.backend.nsl_workspace.dto.ProjectDTO;
import com.backend.nsl_workspace.dto.ProjectMemberDTO;
import com.backend.nsl_workspace.service.ProjectMemberService;
import com.backend.nsl_workspace.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody ProjectDTO projectDTO) {
        try {
            ProjectDTO savedProject = projectService.createProject(projectDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Project created successfully");
            response.put("project", savedProject);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false,
                            "message", "Project creation failed: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProjects() {
        try {
            List<ProjectDTO> projects = projectService.getAllProjects();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("projects", projects);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false,
                            "message", "Failed to fetch projects: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Integer id, @RequestBody ProjectDTO projectDTO) {
        try {
//            ProjectDTO updatedProject = projectService.updateProject(id, projectDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Project updated successfully");
//            response.put("project", updatedProject);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false,
                            "message", "Project update failed: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Integer id) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Project deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false,
                            "message", "Project deletion failed: " + e.getMessage()));
        }
    }

    @GetMapping("/{projectId}/members")
    public ResponseEntity<?> getAllMembers(@PathVariable String projectId) {
        try {
            List<ProjectMemberDTO> members = projectMemberService.getAllMembersByProjectId(projectId);
            return ResponseEntity.ok(Map.of("success", true, "members", members));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Failed to fetch members: " + e.getMessage()));
        }
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity<?> addMember(@PathVariable String projectId, @RequestBody ProjectMemberDTO memberDTO) {
        try {
            memberDTO.setProjectId(projectId);
            ProjectMemberDTO savedMember = projectMemberService.addMember(memberDTO);
            return ResponseEntity.ok(Map.of("success", true, "message", "Member added successfully", "member", savedMember));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Member addition failed: " + e.getMessage()));
        }
    }
}