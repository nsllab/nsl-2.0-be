package com.backend.nsl_workspace.controller.member;

import com.backend.nsl_workspace.entity.MemberType;
import com.backend.nsl_workspace.service.MemberTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberTypeService memberTypeService;

    @GetMapping("/member_type")
    public ResponseEntity<List<MemberType>> getAllMemberTypes() {
        return ResponseEntity.ok(memberTypeService.getAllMemberTypes());
    }
}
