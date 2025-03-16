package com.backend.nsl_workspace.service;

import com.backend.nsl_workspace.dto.PermissionDTO;
import com.backend.nsl_workspace.dto.UserPermissionDTO;
import com.backend.nsl_workspace.entity.Permission;
import com.backend.nsl_workspace.entity.UserPermission;
import com.backend.nsl_workspace.repository.PermissionRepository;
import com.backend.nsl_workspace.repository.UserPermissionRepository;
import com.backend.nsl_workspace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all available permissions
     */
    public List<PermissionDTO> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(p -> new PermissionDTO(p.getPermissionId(), p.getPermissionName()))
                .collect(Collectors.toList());
    }

    /**
     * Get all permissions for a specific user
     */
    public List<UserPermissionDTO> getUserPermissions(Integer userId) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        List<UserPermission> userPermissions = userPermissionRepository.findByUserId(userId);
        List<UserPermissionDTO> userPermissionDTOs = new ArrayList<>();

        for (UserPermission up : userPermissions) {
            Permission permission = permissionRepository.findById(up.getPermissionId()).orElse(null);
            if (permission != null) {
                userPermissionDTOs.add(new UserPermissionDTO(
                        up.getUserId(),
                        permission.getPermissionId(),
                        permission.getPermissionName()
                ));
            }
        }

        return userPermissionDTOs;
    }

    /**
     * Check if user has a specific permission
     */
    public boolean hasPermission(Integer userId, Integer permissionId) {
        return userPermissionRepository.hasPermission(userId, permissionId);
    }

    /**
     * Assign permissions to a user
     * @return Number of newly assigned permissions
     */
    @Transactional
    public int assignPermissions(Integer userId, List<Integer> permissionIds) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        int assignedCount = 0;

        for (Integer permissionId : permissionIds) {
            // Verify permission exists
            if (!permissionRepository.existsById(permissionId)) {
                continue; // Skip invalid permissions
            }

            // Check if already assigned
            if (userPermissionRepository.hasPermission(userId, permissionId)) {
                continue; // Skip already assigned permissions
            }

            // Assign permission
            UserPermission userPermission = new UserPermission();
            userPermission.setUserId(userId);
            userPermission.setPermissionId(permissionId);
            userPermissionRepository.save(userPermission);
            assignedCount++;
        }

        return assignedCount;
    }

    /**
     * Revoke a permission from a user
     */
    @Transactional
    public boolean revokePermission(Integer userId, Integer permissionId) {
        // Create the composite key
        UserPermission.UserPermissionId id = new UserPermission.UserPermissionId(userId, permissionId);

        // Check if the permission exists
        if (!userPermissionRepository.existsById(id)) {
            return false;
        }

        // Revoke permission
        userPermissionRepository.deleteById(id);
        return true;
    }

    /**
     * Revoke all permissions from a user
     */
    @Transactional
    public void revokeAllPermissions(Integer userId) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        userPermissionRepository.deleteByUserId(userId);
    }

    /**
     * Update all permissions for a user based on a complete list
     * @return Map with counts of added and removed permissions
     */
    @Transactional
    public Map<String, Integer> updateUserPermissions(Integer userId, List<Integer> selectedPermissionIds) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        // Get current permissions
        List<UserPermission> currentPermissions = userPermissionRepository.findByUserId(userId);
        List<Integer> currentPermissionIds = currentPermissions.stream()
                .map(UserPermission::getPermissionId)
                .collect(Collectors.toList());

        // Track changes
        int addedCount = 0;
        int removedCount = 0;

        // Remove permissions that are no longer selected
        for (UserPermission perm : currentPermissions) {
            if (!selectedPermissionIds.contains(perm.getPermissionId())) {
                userPermissionRepository.delete(perm);
                removedCount++;
            }
        }

        // Add new selected permissions
        for (Integer permId : selectedPermissionIds) {
            if (!currentPermissionIds.contains(permId) && permissionRepository.existsById(permId)) {
                UserPermission newPerm = new UserPermission();
                newPerm.setUserId(userId);
                newPerm.setPermissionId(permId);
                userPermissionRepository.save(newPerm);
                addedCount++;
            }
        }

        Map<String, Integer> result = new HashMap<>();
        result.put("added", addedCount);
        result.put("removed", removedCount);

        return result;
    }

    /**
     * Add default permissions when a new user is registered
     */
    @Transactional
    public void assignDefaultPermissionsToUser(Integer userId) {
        try {
            // Verify user exists
            if (!userRepository.existsById(userId)) {
                throw new IllegalArgumentException("User not found");
            }

            // List of view-related permission IDs
            // This includes only viewing permissions
            List<Integer> viewPermissionIds = Arrays.asList(
                    1,  // view_users
                    10, // view_publication
                    17, // view_lab_equipment
                    22, // view_lab_inventory
                    27, // view_project
                    37, // view_notice
                    42  // view_dashboard
            );

            // Assign view permissions
            for (Integer permissionId : viewPermissionIds) {
                if (!userPermissionRepository.hasPermission(userId, permissionId)) {
                    UserPermission userPermission = new UserPermission();
                    userPermission.setUserId(userId);
                    userPermission.setPermissionId(permissionId);
                    userPermissionRepository.save(userPermission);
                }
            }
        } catch (Exception e) {
            // Log the error
            System.err.println("Error assigning default permissions: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Revoke multiple permissions from a user
     * @param userId The user ID
     * @param permissionIds List of permission IDs to revoke
     * @return Number of permissions successfully revoked
     */
    @Transactional
    public int revokeMultiplePermissions(Integer userId, List<Integer> permissionIds) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        int revokedCount = 0;

        for (Integer permissionId : permissionIds) {
            // Create the composite key
            UserPermission.UserPermissionId id = new UserPermission.UserPermissionId(userId, permissionId);

            // Check if the permission exists and revoke it
            if (userPermissionRepository.existsById(id)) {
                userPermissionRepository.deleteById(id);
                revokedCount++;
            }
        }

        return revokedCount;
    }
}