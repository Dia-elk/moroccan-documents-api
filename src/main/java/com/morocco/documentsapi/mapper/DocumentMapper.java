package com.morocco.documentsapi.mapper;

import com.morocco.documentsapi.dto.CategoryDto;
import com.morocco.documentsapi.dto.DocumentDto;
import com.morocco.documentsapi.dto.LocationDto;
import com.morocco.documentsapi.dto.ProcedureDto;
import com.morocco.documentsapi.dto.RequirementDto;
import com.morocco.documentsapi.enums.DocumentCategoryEnum;
import com.morocco.documentsapi.model.Document;
import com.morocco.documentsapi.model.DocumentLocation;
import com.morocco.documentsapi.model.DocumentProcedure;
import com.morocco.documentsapi.model.DocumentRequirement;

import java.util.List;

/**
 * Pure, static entity-to-DTO conversion. No Spring bean, no business logic —
 * every field that differs by language is resolved here from the requested
 * {@code lang} ("fr" or "ar", already validated upstream).
 */
public final class DocumentMapper {

    private DocumentMapper() {
    }

    public static DocumentDto toDto(Document entity, String lang) {
        boolean ar = "ar".equals(lang);
        return DocumentDto.builder()
                .code(entity.getCode())
                .name(ar ? entity.getNameAr() : entity.getNameFr())
                .description(ar ? entity.getDescriptionAr() : entity.getDescriptionFr())
                .category(entity.getCategory().name().toLowerCase())
                .categoryLabel(entity.getCategory().getLabel(lang))
                .feeAmount(entity.getFeeMad())
                .feeCurrency(entity.getFeeCurrency())
                .feeVariable(entity.isFeeVariable())
                .feeNote(ar ? entity.getFeeNoteAr() : entity.getFeeNoteFr())
                .processingDays(entity.getProcessingDays())
                .location(ar ? entity.getLocationAr() : entity.getLocationFr())
                .build();
    }

    public static List<DocumentDto> toDtoList(List<Document> entities, String lang) {
        return entities.stream().map(entity -> toDto(entity, lang)).toList();
    }

    public static RequirementDto toRequirementDto(DocumentRequirement entity, String lang) {
        boolean ar = "ar".equals(lang);
        return RequirementDto.builder()
                .requirement(ar ? entity.getRequirementAr() : entity.getRequirementFr())
                .orderIndex(entity.getOrderIndex())
                .build();
    }

    public static ProcedureDto toProcedureDto(DocumentProcedure entity, String lang) {
        boolean ar = "ar".equals(lang);
        return ProcedureDto.builder()
                .step(ar ? entity.getStepAr() : entity.getStepFr())
                .orderIndex(entity.getOrderIndex())
                .build();
    }

    public static LocationDto toLocationDto(DocumentLocation entity, String lang) {
        boolean ar = "ar".equals(lang);
        return LocationDto.builder()
                .name(ar ? entity.getLocationNameAr() : entity.getLocationNameFr())
                .address(ar ? entity.getAddressAr() : entity.getAddressFr())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .workingHours(entity.getWorkingHours())
                .build();
    }

    public static CategoryDto toCategoryDto(DocumentCategoryEnum category, String lang, int documentCount) {
        return CategoryDto.builder()
                .code(category.name().toLowerCase())
                .label(category.getLabel(lang))
                .documentCount(documentCount)
                .build();
    }
}
