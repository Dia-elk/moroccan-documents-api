package com.morocco.documentsapi.repository;

import com.morocco.documentsapi.model.DocumentLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentLocationRepository extends JpaRepository<DocumentLocation, UUID> {

    List<DocumentLocation> findByDocumentId(UUID documentId);
}
