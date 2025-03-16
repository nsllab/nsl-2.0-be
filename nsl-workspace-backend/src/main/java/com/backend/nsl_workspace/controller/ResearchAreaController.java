package com.backend.nsl_workspace.controller;

import com.backend.nsl_workspace.entity.ResearchArea;
import com.backend.nsl_workspace.service.ResearchAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ResearchAreaController {

    @Autowired
    private ResearchAreaService researchAreaService;

    @GetMapping("/research_area")
    public ResponseEntity<List<ResearchArea>> getAllResearchAreas() {
        return ResponseEntity.ok(researchAreaService.getAllResearchAreas());
    }
}