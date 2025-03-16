package com.backend.nsl_workspace.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "user_permission_tbl")
@IdClass(UserPermission.UserPermissionId.class)
public class UserPermission {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "permission_id")
    private Integer permissionId;

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

    // Composite key class
    public static class UserPermissionId implements Serializable {
        private Integer userId;
        private Integer permissionId;

        public UserPermissionId() {
        }

        public UserPermissionId(Integer userId, Integer permissionId) {
            this.userId = userId;
            this.permissionId = permissionId;
        }

        // Equals and HashCode implementations
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UserPermissionId that = (UserPermissionId) o;

            if (!userId.equals(that.userId)) return false;
            return permissionId.equals(that.permissionId);
        }

        @Override
        public int hashCode() {
            int result = userId.hashCode();
            result = 31 * result + permissionId.hashCode();
            return result;
        }
    }
}