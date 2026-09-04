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
@Table(name = "document_procedures", indexes = {
        @Index(name = "idx_doc_procedures_document_id", columnList = "document_id")
})
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentProcedure extends BaseEntity {

    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(name = "step_fr", nullable = false, columnDefinition = "TEXT")
    private String stepFr;

    @Column(name = "step_ar", nullable = false, columnDefinition = "TEXT")
    private String stepAr;

    @Column(name = "order_index")
    private Integer orderIndex;
}
