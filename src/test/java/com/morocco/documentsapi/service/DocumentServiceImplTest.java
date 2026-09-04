package com.morocco.documentsapi.service;

import com.morocco.documentsapi.dto.CategoryListData;
import com.morocco.documentsapi.dto.DocumentDetailData;
import com.morocco.documentsapi.dto.DocumentListData;
import com.morocco.documentsapi.dto.DocumentSearchData;
import com.morocco.documentsapi.enums.DocumentCategoryEnum;
import com.morocco.documentsapi.exception.DocumentNotFoundException;
import com.morocco.documentsapi.exception.ErrorCode;
import com.morocco.documentsapi.model.Document;
import com.morocco.documentsapi.model.DocumentLocation;
import com.morocco.documentsapi.model.DocumentProcedure;
import com.morocco.documentsapi.model.DocumentRequirement;
import com.morocco.documentsapi.repository.DocumentLocationRepository;
import com.morocco.documentsapi.repository.DocumentProcedureRepository;
import com.morocco.documentsapi.repository.DocumentRepository;
import com.morocco.documentsapi.repository.DocumentRequirementRepository;
import com.morocco.documentsapi.service.impl.DocumentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private DocumentRequirementRepository requirementRepository;
    @Mock
    private DocumentProcedureRepository procedureRepository;
    @Mock
    private DocumentLocationRepository locationRepository;

    @InjectMocks
    private DocumentServiceImpl documentService;

    private Document sampleDocument() {
        Document document = Document.builder()
                .code("national-id")
                .nameFr("Carte Nationale d'Identité Électronique")
                .nameAr("البطاقة الوطنية للتعريف الإلكترونية")
                .descriptionFr("Description FR")
                .descriptionAr("وصف بالعربية")
                .category(DocumentCategoryEnum.IDENTITY)
                .feeMad(new BigDecimal("100.00"))
                .feeCurrency("MAD")
                .processingDays(15)
                .locationFr("Préfecture")
                .locationAr("العمالة")
                .build();
        // id is normally assigned by BaseEntity's @PrePersist on a real save();
        // set explicitly here since these repositories are mocked, not backed by JPA.
        document.setId(UUID.randomUUID());
        return document;
    }

    @Test
    void getAllDocuments_frenchLanguage_returnsFrenchNames() {
        when(documentRepository.findAllByOrderByNameFrAsc()).thenReturn(List.of(sampleDocument()));

        DocumentListData result = documentService.getAllDocuments("fr");

        assertEquals(1, result.getCount());
        assertEquals("Carte Nationale d'Identité Électronique", result.getDocuments().get(0).getName());
    }

    @Test
    void getAllDocuments_arabicLanguage_returnsArabicNames() {
        when(documentRepository.findAllByOrderByNameFrAsc()).thenReturn(List.of(sampleDocument()));

        DocumentListData result = documentService.getAllDocuments("ar");

        assertEquals(1, result.getCount());
        assertEquals("البطاقة الوطنية للتعريف الإلكترونية", result.getDocuments().get(0).getName());
    }

    @Test
    void getDocumentByCode_found_returnsDetailWithChildren() {
        Document document = sampleDocument();
        when(documentRepository.findByCode("national-id")).thenReturn(Optional.of(document));
        when(requirementRepository.findByDocumentIdOrderByOrderIndexAsc(document.getId()))
                .thenReturn(List.of(DocumentRequirement.builder()
                        .documentId(document.getId()).requirementFr("Req FR").requirementAr("متطلب").orderIndex(1).build()));
        when(procedureRepository.findByDocumentIdOrderByOrderIndexAsc(document.getId()))
                .thenReturn(List.of(DocumentProcedure.builder()
                        .documentId(document.getId()).stepFr("Step FR").stepAr("خطوة").orderIndex(1).build()));
        when(locationRepository.findByDocumentId(document.getId()))
                .thenReturn(List.of(DocumentLocation.builder()
                        .documentId(document.getId()).locationNameFr("Bureau").locationNameAr("مكتب").build()));

        DocumentDetailData result = documentService.getDocumentByCode("national-id", "fr");

        assertEquals("national-id", result.getDocument().getCode());
        assertEquals(1, result.getRequirements().size());
        assertEquals(1, result.getProcedures().size());
        assertEquals(1, result.getLocations().size());
    }

    @Test
    void getDocumentByCode_notFound_throwsDocumentNotFoundException() {
        when(documentRepository.findByCode("unknown")).thenReturn(Optional.empty());

        DocumentNotFoundException exception = assertThrows(DocumentNotFoundException.class,
                () -> documentService.getDocumentByCode("unknown", "fr"));

        assertEquals(ErrorCode.DOC_001, exception.getErrorCode());
    }

    @Test
    void searchDocuments_returnsMatchingDocuments() {
        when(documentRepository.search("passeport")).thenReturn(List.of(sampleDocument()));

        DocumentSearchData result = documentService.searchDocuments("passeport", "fr");

        assertEquals(1, result.getCount());
        assertEquals("passeport", result.getQuery());
    }

    @Test
    void getCategories_returnsAllCategoriesWithCounts() {
        when(documentRepository.findAll()).thenReturn(List.of(sampleDocument()));

        CategoryListData result = documentService.getCategories("fr");

        assertEquals(DocumentCategoryEnum.values().length, result.getCategories().size());
        assertThat(result.getCategories())
                .filteredOn(c -> c.getCode().equals("identity"))
                .first()
                .satisfies(c -> assertEquals(1, c.getDocumentCount()));
    }
}
