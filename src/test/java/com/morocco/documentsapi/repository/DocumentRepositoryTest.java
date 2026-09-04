package com.morocco.documentsapi.repository;

import com.morocco.documentsapi.enums.DocumentCategoryEnum;
import com.morocco.documentsapi.model.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class DocumentRepositoryTest {

    @Autowired
    private DocumentRepository documentRepository;

    private Document buildDocument(String code, String nameFr, String nameAr) {
        return Document.builder()
                .code(code)
                .nameFr(nameFr)
                .nameAr(nameAr)
                .descriptionFr("Description")
                .descriptionAr("وصف")
                .category(DocumentCategoryEnum.IDENTITY)
                .feeMad(new BigDecimal("100.00"))
                .processingDays(10)
                .locationFr("Location")
                .locationAr("موقع")
                .build();
    }

    @Test
    void findByCode_existingCode_returnsDocument() {
        documentRepository.save(buildDocument("national-id", "Carte Nationale", "البطاقة الوطنية"));

        Optional<Document> found = documentRepository.findByCode("national-id");

        assertTrue(found.isPresent());
        assertEquals("Carte Nationale", found.get().getNameFr());
    }

    @Test
    void findByCode_nonExistingCode_returnsEmpty() {
        Optional<Document> found = documentRepository.findByCode("does-not-exist");
        assertFalse(found.isPresent());
    }

    @Test
    void search_matchesFrenchName() {
        documentRepository.save(buildDocument("passport", "Passeport Marocain", "جواز السفر المغربي"));

        List<Document> results = documentRepository.search("passeport");

        assertEquals(1, results.size());
        assertEquals("passport", results.get(0).getCode());
    }

    @Test
    void search_matchesArabicName() {
        documentRepository.save(buildDocument("passport", "Passeport Marocain", "جواز السفر المغربي"));

        List<Document> results = documentRepository.search("جواز");

        assertEquals(1, results.size());
    }

    @Test
    void existsByCode_existingCode_returnsTrue() {
        documentRepository.save(buildDocument("national-id", "Carte Nationale", "البطاقة الوطنية"));
        assertTrue(documentRepository.existsByCode("national-id"));
    }

    @Test
    void existsByCode_nonExistingCode_returnsFalse() {
        assertFalse(documentRepository.existsByCode("does-not-exist"));
    }
}
