package com.backend.nsl_workspace.service;

import com.backend.nsl_workspace.entity.ResearchArea;
import com.backend.nsl_workspace.repository.ResearchAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResearchAreaService {

    @Autowired
    private ResearchAreaRepository researchAreaRepository;

    public List<ResearchArea> getAllResearchAreas() {
        return researchAreaRepository.findAll();
    }
}