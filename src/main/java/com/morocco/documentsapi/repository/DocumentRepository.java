package com.morocco.documentsapi.repository;

import com.morocco.documentsapi.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {

    Optional<Document> findByCode(String code);

    boolean existsByCode(String code);

    List<Document> findAllByOrderByNameFrAsc();

    @Query("""
            SELECT d FROM Document d WHERE
            LOWER(d.code) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(d.nameFr) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(d.nameAr) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(d.descriptionFr) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(d.descriptionAr) LIKE LOWER(CONCAT('%', :query, '%'))
            ORDER BY d.nameFr ASC
            """)
    List<Document> search(@Param("query") String query);
}
