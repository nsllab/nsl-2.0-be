package com.backend.nsl_workspace.controller;

import com.backend.nsl_workspace.entity.Funding;
import com.backend.nsl_workspace.service.FundingService;
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
public class FundingController {

    @Autowired
    private FundingService fundingService;

    @GetMapping("/funding")
    public ResponseEntity<List<Funding>> getAllFundings() {
        return ResponseEntity.ok(fundingService.getAllFundings());
    }
}