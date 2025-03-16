package com.backend.nsl_workspace.service;

import com.backend.nsl_workspace.dto.ProjectMemberDTO;
import com.backend.nsl_workspace.entity.Project;
import com.backend.nsl_workspace.entity.ProjectMember;
import com.backend.nsl_workspace.entity.User;
import com.backend.nsl_workspace.repository.ProjectMemberRepository;
import com.backend.nsl_workspace.repository.ProjectRepository;
import com.backend.nsl_workspace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectMemberService {

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ProjectMemberDTO> getAllMembersByProjectId(String projectId) {
        List<ProjectMember> members = projectMemberRepository.findByProjectId(projectId);
        return members.stream().map(member -> {
            ProjectMemberDTO dto = new ProjectMemberDTO();
            dto.setProjectId(member.getProjectId());
            dto.setUserId(member.getUserId());
            dto.setRole(member.getRole().name());
            dto.setJoinDate(member.getJoinDate());
            dto.setEndDate(member.getEndDate());
            return dto;
        }).collect(Collectors.toList());
    }

    public ProjectMemberDTO addMember(ProjectMemberDTO memberDTO) {
        Optional<Project> project = projectRepository.findByProjectId(memberDTO.getProjectId());
        Optional<User> user = userRepository.findById(memberDTO.getUserId());

        if (project.isEmpty() || user.isEmpty()) {
            throw new RuntimeException("Project or User not found");
        }

        ProjectMember member = new ProjectMember();
        member.setProjectId(memberDTO.getProjectId());
        member.setUserId(memberDTO.getUserId());
        member.setRole(ProjectMember.Role.valueOf(memberDTO.getRole()));
        member.setJoinDate(memberDTO.getJoinDate());
        member.setEndDate(memberDTO.getEndDate());

        ProjectMember savedMember = projectMemberRepository.save(member);
        memberDTO.setRole(savedMember.getRole().name());
        return memberDTO;
    }
}
