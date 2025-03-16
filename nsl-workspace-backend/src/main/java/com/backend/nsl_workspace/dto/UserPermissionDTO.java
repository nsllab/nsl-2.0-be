package com.backend.nsl_workspace.dto;

/**
 * Data Transfer Object for User Permission
 */
public class UserPermissionDTO {
    private Integer userId;
    private Integer permissionId;
    private String permissionName;

    // Default constructor
    public UserPermissionDTO() {
    }

    // Constructor with fields
    public UserPermissionDTO(Integer userId, Integer permissionId, String permissionName) {
        this.userId = userId;
        this.permissionId = permissionId;
        this.permissionName = permissionName;
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}