package com.backend.nsl_workspace.repository;

import com.backend.nsl_workspace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query(value = "SELECT MAX(SUBSTRING(user_id, 9)) FROM user_tbl WHERE SUBSTRING(user_id, 1, 8) = :datePrefix", nativeQuery = true)
    Integer findLatestIdForDate(@Param("datePrefix") String datePrefix);

    @Query(value = "SELECT * FROM user_tbl WHERE username LIKE %:query% OR firstname LIKE %:query% OR lastname LIKE %:query%",
            nativeQuery = true)
    List<User> findUsersByNameLike(@Param("query") String query);
}
