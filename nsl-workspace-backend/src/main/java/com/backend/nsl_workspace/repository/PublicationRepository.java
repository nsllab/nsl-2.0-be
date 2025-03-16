package com.backend.nsl_workspace.repository;

import com.backend.nsl_workspace.entity.Publication;
import com.backend.nsl_workspace.entity.PublicationJunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Integer> {
}