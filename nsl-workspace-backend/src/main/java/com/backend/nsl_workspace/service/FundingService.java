package com.backend.nsl_workspace.service;

import com.backend.nsl_workspace.entity.Funding;
import com.backend.nsl_workspace.repository.FundingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundingService {

    @Autowired
    private FundingRepository fundingRepository;

    public List<Funding> getAllFundings() {
        return fundingRepository.findAll();
    }
}