package com.morocco.documentsapi.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Categories used to group Moroccan administrative documents.
 * Stored in the database as a lowercase string via {@link DocumentCategoryEnumConverter}.
 */
public enum DocumentCategoryEnum {

    IDENTITY("Identité", "الهوية"),
    TRAVEL("Voyage", "السفر"),
    TRANSPORT("Transport", "النقل"),
    CIVIL_STATUS("État Civil", "الحالة المدنية"),
    COMMERCIAL("Commercial", "تجاري"),
    PROPERTY("Propriété", "العقار"),
    RESIDENCE("Résidence", "الإقامة");

    private final String labelFr;
    private final String labelAr;

    DocumentCategoryEnum(String labelFr, String labelAr) {
        this.labelFr = labelFr;
        this.labelAr = labelAr;
    }

    public String getLabel(String lang) {
        return "ar".equals(lang) ? labelAr : labelFr;
    }

    @Converter(autoApply = true)
    public static class DocumentCategoryEnumConverter implements AttributeConverter<DocumentCategoryEnum, String> {

        @Override
        public String convertToDatabaseColumn(DocumentCategoryEnum attribute) {
            if (attribute == null) return null;
            return attribute.name().toLowerCase();
        }

        @Override
        public DocumentCategoryEnum convertToEntityAttribute(String dbData) {
            if (dbData == null) return null;
            return DocumentCategoryEnum.valueOf(dbData.toUpperCase());
        }
    }
}
