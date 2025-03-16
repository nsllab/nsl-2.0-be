package com.backend.nsl_workspace.repository;

import com.backend.nsl_workspace.entity.PaperAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaperAuthorRepository extends JpaRepository<PaperAuthor, Integer> {
    List<PaperAuthor> findByPaperId(Integer paperId);
    List<PaperAuthor> findByUserId(Integer userId);
    void deleteByPaperId(Integer paperId);

    @Query(value = "SELECT DISTINCT pa.user_id FROM paper_author pa " +
            "JOIN user u ON pa.user_id = u.user_id " +
            "WHERE u.username LIKE %:query% OR u.first_name LIKE %:query% OR u.last_name LIKE %:query%",
            nativeQuery = true)
    List<Integer> findUsersByNameLike(@Param("query") String query);
}