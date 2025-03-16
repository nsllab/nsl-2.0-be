package com.backend.nsl_workspace.dto;

import com.backend.nsl_workspace.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserDTO {
    private Integer userId;
    private Integer memberTypeId;
    private String lastName;
    private String firstName;
    private String middleName;
    private String email;
    private String username;
    private String koreanName;
    private String profileImgPath;
    private LocalDate joinDate;
    private LocalDate gradDate;
    private String contInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public UserDTO() {
    }

    // Static method to convert User entity to UserDTO
    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setMemberTypeId(user.getMemberTypeId());
        dto.setLastName(user.getLastName());
        dto.setFirstName(user.getFirstName());
        dto.setMiddleName(user.getMiddleName());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setKoreanName(user.getKoreanName());
        dto.setProfileImgPath(user.getProfileImgPath());
        dto.setJoinDate(user.getJoinDate());
        dto.setGradDate(user.getGradDate());
        dto.setContInfo(user.getContInfo());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    // Method to convert DTO to User entity
    public User toEntity() {
        User user = new User();
        user.setUserId(this.userId);
        user.setMemberTypeId(this.memberTypeId);
        user.setLastName(this.lastName);
        user.setFirstName(this.firstName);
        user.setMiddleName(this.middleName);
        user.setEmail(this.email);
        user.setUsername(this.username);
        user.setKoreanName(this.koreanName);
        user.setProfileImgPath(this.profileImgPath);
        user.setJoinDate(this.joinDate);
        user.setGradDate(this.gradDate);
        user.setContInfo(this.contInfo);
        user.setCreatedAt(this.createdAt);
        user.setUpdatedAt(this.updatedAt);
        return user;
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMemberTypeId() {
        return memberTypeId;
    }

    public void setMemberTypeId(Integer memberTypeId) {
        this.memberTypeId = memberTypeId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKoreanName() {
        return koreanName;
    }

    public void setKoreanName(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getProfileImgPath() {
        return profileImgPath;
    }

    public void setProfileImgPath(String profileImgPath) {
        this.profileImgPath = profileImgPath;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public LocalDate getGradDate() {
        return gradDate;
    }

    public void setGradDate(LocalDate gradDate) {
        this.gradDate = gradDate;
    }

    public String getContInfo() {
        return contInfo;
    }

    public void setContInfo(String contInfo) {
        this.contInfo = contInfo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Note: I've intentionally excluded the password field for security reasons
}