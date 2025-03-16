package com.backend.nsl_workspace.repository;

import com.backend.nsl_workspace.entity.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, UserPermission.UserPermissionId> {

    // Find all permissions for a specific user
    List<UserPermission> findByUserId(Integer userId);

    // Delete all permissions for a specific user
    @Modifying
    @Transactional
    @Query("DELETE FROM UserPermission up WHERE up.userId = :userId")
    void deleteByUserId(@Param("userId") Integer userId);

    // Check if a user has a specific permission
    @Query("SELECT COUNT(up) > 0 FROM UserPermission up WHERE up.userId = :userId AND up.permissionId = :permissionId")
    boolean hasPermission(@Param("userId") Integer userId, @Param("permissionId") Integer permissionId);

    // Find users with a specific permission
    @Query("SELECT up.userId FROM UserPermission up WHERE up.permissionId = :permissionId")
    List<Integer> findUserIdsByPermissionId(@Param("permissionId") Integer permissionId);
}