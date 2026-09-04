package com.morocco.documentsapi.service.impl;

import com.morocco.documentsapi.dto.CategoryListData;
import com.morocco.documentsapi.dto.DocumentDetailData;
import com.morocco.documentsapi.dto.DocumentListData;
import com.morocco.documentsapi.dto.DocumentSearchData;
import com.morocco.documentsapi.enums.DocumentCategoryEnum;
import com.morocco.documentsapi.exception.DocumentNotFoundException;
import com.morocco.documentsapi.mapper.DocumentMapper;
import com.morocco.documentsapi.model.Document;
import com.morocco.documentsapi.repository.DocumentLocationRepository;
import com.morocco.documentsapi.repository.DocumentProcedureRepository;
import com.morocco.documentsapi.repository.DocumentRepository;
import com.morocco.documentsapi.repository.DocumentRequirementRepository;
import com.morocco.documentsapi.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentRequirementRepository requirementRepository;
    private final DocumentProcedureRepository procedureRepository;
    private final DocumentLocationRepository locationRepository;

    @Override
    public DocumentListData getAllDocuments(String lang) {
        List<Document> documents = documentRepository.findAllByOrderByNameFrAsc();
        log.info("Listing {} documents (lang={})", documents.size(), lang);
        return DocumentListData.builder()
                .count(documents.size())
                .documents(DocumentMapper.toDtoList(documents, lang))
                .build();
    }

    @Override
    public DocumentDetailData getDocumentByCode(String code, String lang) {
        Document document = documentRepository.findByCode(code)
                .orElseThrow(DocumentNotFoundException::new);

        var requirements = requirementRepository.findByDocumentIdOrderByOrderIndexAsc(document.getId()).stream()
                .map(r -> DocumentMapper.toRequirementDto(r, lang))
                .toList();
        var procedures = procedureRepository.findByDocumentIdOrderByOrderIndexAsc(document.getId()).stream()
                .map(p -> DocumentMapper.toProcedureDto(p, lang))
                .toList();
        var locations = locationRepository.findByDocumentId(document.getId()).stream()
                .map(l -> DocumentMapper.toLocationDto(l, lang))
                .toList();

        log.info("Fetched document '{}' with {} requirements, {} procedures, {} locations (lang={})",
                code, requirements.size(), procedures.size(), locations.size(), lang);

        return DocumentDetailData.builder()
                .document(DocumentMapper.toDto(document, lang))
                .requirements(requirements)
                .procedures(procedures)
                .locations(locations)
                .build();
    }

    @Override
    public DocumentSearchData searchDocuments(String query, String lang) {
        List<Document> results = documentRepository.search(query.trim());
        log.info("Search '{}' returned {} results (lang={})", query, results.size(), lang);
        return DocumentSearchData.builder()
                .query(query)
                .count(results.size())
                .documents(DocumentMapper.toDtoList(results, lang))
                .build();
    }

    @Override
    public CategoryListData getCategories(String lang) {
        Map<DocumentCategoryEnum, Long> counts = documentRepository.findAll().stream()
                .collect(Collectors.groupingBy(Document::getCategory, Collectors.counting()));

        var categories = Arrays.stream(DocumentCategoryEnum.values())
                .map(category -> DocumentMapper.toCategoryDto(category, lang, counts.getOrDefault(category, 0L).intValue()))
                .toList();

        return CategoryListData.builder()
                .categories(categories)
                .build();
    }
}
