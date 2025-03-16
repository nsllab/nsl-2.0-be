package com.backend.nsl_workspace.repository;

import com.backend.nsl_workspace.entity.MemberType;
import com.backend.nsl_workspace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberTypeRepository extends JpaRepository<MemberType, Long> {
    Optional<MemberType> findById(Integer id);
}