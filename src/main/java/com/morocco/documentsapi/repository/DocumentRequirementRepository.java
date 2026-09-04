package com.morocco.documentsapi.repository;

import com.morocco.documentsapi.model.DocumentRequirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentRequirementRepository extends JpaRepository<DocumentRequirement, UUID> {

    List<DocumentRequirement> findByDocumentIdOrderByOrderIndexAsc(UUID documentId);
}
