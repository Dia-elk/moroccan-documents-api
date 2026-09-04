package com.morocco.documentsapi.repository;

import com.morocco.documentsapi.model.DocumentProcedure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentProcedureRepository extends JpaRepository<DocumentProcedure, UUID> {

    List<DocumentProcedure> findByDocumentIdOrderByOrderIndexAsc(UUID documentId);
}
