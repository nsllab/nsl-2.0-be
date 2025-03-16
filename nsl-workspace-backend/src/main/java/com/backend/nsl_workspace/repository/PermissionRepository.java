package com.backend.nsl_workspace.repository;

import com.backend.nsl_workspace.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    // Custom query to find all permission IDs
    @Query("SELECT p.permissionId FROM Permission p")
    List<Integer> findAllPermissionIds();

    // Find permission by name
    Permission findByPermissionName(String permissionName);
}