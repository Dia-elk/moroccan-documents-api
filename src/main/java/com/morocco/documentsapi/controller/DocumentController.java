package com.morocco.documentsapi.controller;

import com.morocco.documentsapi.dto.ApiResponse;
import com.morocco.documentsapi.dto.DocumentDetailData;
import com.morocco.documentsapi.dto.DocumentListData;
import com.morocco.documentsapi.dto.DocumentSearchData;
import com.morocco.documentsapi.service.DocumentService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Validated
public class DocumentController {

    private static final String LANG_PATTERN = "fr|ar";

    private final DocumentService documentService;

    @GetMapping
    public ResponseEntity<ApiResponse<DocumentListData>> getAllDocuments(
            @RequestParam(defaultValue = "fr") @Pattern(regexp = LANG_PATTERN, message = "DOC_002") String lang) {
        return ResponseEntity.ok(ApiResponse.ok(lang, documentService.getAllDocuments(lang)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<DocumentSearchData>> searchDocuments(
            @RequestParam @NotBlank(message = "DOC_003") String query,
            @RequestParam(defaultValue = "fr") @Pattern(regexp = LANG_PATTERN, message = "DOC_002") String lang) {
        return ResponseEntity.ok(ApiResponse.ok(lang, documentService.searchDocuments(query, lang)));
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<DocumentDetailData>> getDocumentByCode(
            @PathVariable String code,
            @RequestParam(defaultValue = "fr") @Pattern(regexp = LANG_PATTERN, message = "DOC_002") String lang) {
        return ResponseEntity.ok(ApiResponse.ok(lang, documentService.getDocumentByCode(code, lang)));
    }
}
