package com.backend.nsl_workspace.service;

import com.backend.nsl_workspace.entity.MemberType;
import com.backend.nsl_workspace.repository.MemberTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberTypeService {

    @Autowired
    private MemberTypeRepository memberTypeRepository;

    public List<MemberType> getAllMemberTypes() {
        return memberTypeRepository.findAll();
    }

    public String getMemberTypeName(Integer memberTypeId) {
        return memberTypeRepository.findById(memberTypeId)
                .map(MemberType::getMemberTypeName)
                .orElse("Unknown"); // Or handle the not-found case differently
    }
}
