package com.backend.nsl_workspace.controller;

import com.backend.nsl_workspace.dto.PermissionDTO;
import com.backend.nsl_workspace.dto.PermissionRequestDTO;
import com.backend.nsl_workspace.dto.ResponseDTO;
import com.backend.nsl_workspace.dto.UserPermissionDTO;
import com.backend.nsl_workspace.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/permissions")
@CrossOrigin(origins = "*")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * Get all available permissions
     */
    @GetMapping
    public ResponseEntity<ResponseDTO<List<PermissionDTO>>> getAllPermissions() {
        try {
            List<PermissionDTO> permissions = permissionService.getAllPermissions();
            return ResponseEntity.ok(ResponseDTO.success("Permissions retrieved successfully", permissions));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Failed to fetch permissions: " + e.getMessage()));
        }
    }

    /**
     * Get permissions for a specific user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDTO<List<UserPermissionDTO>>> getUserPermissions(@PathVariable Integer userId) {
        try {
            List<UserPermissionDTO> userPermissions = permissionService.getUserPermissions(userId);
            return ResponseEntity.ok(ResponseDTO.success("User permissions retrieved successfully", userPermissions));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Failed to fetch user permissions: " + e.getMessage()));
        }
    }

    /**
     * Check if a user has a specific permission
     */
    @GetMapping("/user/{userId}/has/{permissionId}")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> checkUserPermission(
            @PathVariable Integer userId,
            @PathVariable Integer permissionId) {
        try {
            boolean hasPermission = permissionService.hasPermission(userId, permissionId);

            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("permissionId", permissionId);
            data.put("hasPermission", hasPermission);

            return ResponseEntity.ok(ResponseDTO.success("Permission check completed", data));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Failed to check user permission: " + e.getMessage()));
        }
    }

    /**
     * Assign permissions to a user
     */
    @PostMapping("/assign")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> assignPermissions(@RequestBody PermissionRequestDTO request) {
        try {
            Integer userId = request.getUserId();
            List<Integer> permissionIds;

            if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
                // Handle multiple permissions
                permissionIds = request.getPermissionIds();
            } else if (request.getPermissionId() != null) {
                // Handle single permission
                permissionIds = List.of(request.getPermissionId());
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseDTO.error("No permissions specified"));
            }

            int assignedCount = permissionService.assignPermissions(userId, permissionIds);

            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("assignedCount", assignedCount);

            return ResponseEntity.ok(
                    ResponseDTO.success(assignedCount + " permission(s) assigned successfully", data));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Failed to assign permissions: " + e.getMessage()));
        }
    }

    /**
     * Revoke a permission from a user
     */
    @DeleteMapping("/revoke")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> revokePermissions(@RequestBody Map<String, Object> payload) {
        try {
            Integer userId = (Integer) payload.get("userId");

            // Handle both single permission and list of permissions
            Object permissionObj = payload.get("permissionId");
            List<Integer> permissionIds = new ArrayList<>();

            if (permissionObj instanceof Integer) {
                // Single permission ID
                permissionIds.add((Integer) permissionObj);
            } else if (payload.containsKey("permissionIds") && payload.get("permissionIds") instanceof List) {
                // List of permission IDs
                @SuppressWarnings("unchecked")
                List<Integer> permissions = (List<Integer>) payload.get("permissionIds");
                permissionIds.addAll(permissions);
            } else {
                return ResponseEntity.badRequest()
                        .body(ResponseDTO.error("Invalid permission format. Expected permissionId as Integer or permissionIds as List of Integers"));
            }

            if (userId == null || permissionIds.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ResponseDTO.error("User ID and at least one Permission ID are required"));
            }

            int revokedCount = permissionService.revokeMultiplePermissions(userId, permissionIds);

            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("revokedCount", revokedCount);

            return ResponseEntity.ok(
                    ResponseDTO.success(revokedCount + " permission(s) revoked successfully", data));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Failed to revoke permissions: " + e.getMessage()));
        }
    }

    /**
     * Revoke all permissions from a user
     */
    @DeleteMapping("/revoke-all/{userId}")
    public ResponseEntity<ResponseDTO<Void>> revokeAllPermissions(@PathVariable Integer userId) {
        try {
            permissionService.revokeAllPermissions(userId);
            return ResponseEntity.ok(ResponseDTO.success("All permissions revoked successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Failed to revoke permissions: " + e.getMessage()));
        }
    }

    /**
     * Update user permissions based on checkbox selections
     * This method will add and remove permissions to match exactly what was sent
     */
    @PostMapping("/update-all")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> updateUserPermissions(@RequestBody PermissionRequestDTO request) {
        try {
            Integer userId = request.getUserId();
            List<Integer> permissionIds = request.getPermissionIds();

            if (userId == null || permissionIds == null) {
                return ResponseEntity
                        .badRequest()
                        .body(ResponseDTO.error("User ID and permissions list are required"));
            }

            Map<String, Integer> result = permissionService.updateUserPermissions(userId, permissionIds);

            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("added", result.get("added"));
            data.put("removed", result.get("removed"));

            return ResponseEntity.ok(
                    ResponseDTO.success("User permissions updated successfully", data));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Failed to update permissions: " + e.getMessage()));
        }
    }
}