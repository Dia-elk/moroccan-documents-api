package com.morocco.documentsapi.model;

import com.morocco.documentsapi.enums.DocumentCategoryEnum;
import com.morocco.documentsapi.enums.DocumentCategoryEnum.DocumentCategoryEnumConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "documents", indexes = {
        @Index(name = "idx_documents_code", columnList = "code", unique = true)
})
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(name = "name_fr", nullable = false)
    private String nameFr;

    @Column(name = "name_ar", nullable = false)
    private String nameAr;

    @Column(name = "description_fr", columnDefinition = "TEXT")
    private String descriptionFr;

    @Column(name = "description_ar", columnDefinition = "TEXT")
    private String descriptionAr;

    @Convert(converter = DocumentCategoryEnumConverter.class)
    @Column(nullable = false, length = 30)
    private DocumentCategoryEnum category;

    @Column(name = "fee_mad", precision = 12, scale = 2)
    private BigDecimal feeMad;

    @Column(name = "fee_currency", length = 10)
    @Builder.Default
    private String feeCurrency = "MAD";

    @Column(name = "fee_variable", nullable = false)
    @Builder.Default
    private boolean feeVariable = false;

    @Column(name = "fee_note_fr")
    private String feeNoteFr;

    @Column(name = "fee_note_ar")
    private String feeNoteAr;

    @Column(name = "processing_days")
    private Integer processingDays;

    @Column(name = "location_fr")
    private String locationFr;

    @Column(name = "location_ar")
    private String locationAr;
}
