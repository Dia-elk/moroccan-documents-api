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
@Table(name = "document_locations", indexes = {
        @Index(name = "idx_doc_locations_document_id", columnList = "document_id")
})
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentLocation extends BaseEntity {

    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(name = "location_name_fr", nullable = false)
    private String locationNameFr;

    @Column(name = "location_name_ar", nullable = false)
    private String locationNameAr;

    @Column(name = "address_fr", columnDefinition = "TEXT")
    private String addressFr;

    @Column(name = "address_ar", columnDefinition = "TEXT")
    private String addressAr;

    private String phone;

    private String email;

    @Column(name = "working_hours")
    private String workingHours;
}
