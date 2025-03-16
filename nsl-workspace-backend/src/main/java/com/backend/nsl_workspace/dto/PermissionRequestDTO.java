package com.backend.nsl_workspace.dto;

import java.util.List;

/**
 * Data Transfer Object for Permission Requests
 */
public class PermissionRequestDTO {

    // For single permission assignment
    private Integer userId;
    private Integer permissionId;

    // For batch permission assignment
    private List<Integer> permissionIds;

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

    public List<Integer> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Integer> permissionIds) {
        this.permissionIds = permissionIds;
    }
}