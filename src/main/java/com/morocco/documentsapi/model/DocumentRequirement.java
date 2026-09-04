package com.morocco.documentsapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "document_requirements", indexes = {
        @Index(name = "idx_doc_requirements_document_id", columnList = "document_id")
})
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequirement extends BaseEntity {

    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(name = "requirement_fr", nullable = false, columnDefinition = "TEXT")
    private String requirementFr;

    @Column(name = "requirement_ar", nullable = false, columnDefinition = "TEXT")
    private String requirementAr;

    @Column(name = "order_index")
    private Integer orderIndex;
}
