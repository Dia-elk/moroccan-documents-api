package com.morocco.documentsapi.controller;

import com.morocco.documentsapi.dto.ApiResponse;
import com.morocco.documentsapi.dto.CategoryListData;
import com.morocco.documentsapi.service.DocumentService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final DocumentService documentService;

    @GetMapping
    public ResponseEntity<ApiResponse<CategoryListData>> getCategories(
            @RequestParam(defaultValue = "fr") @Pattern(regexp = "fr|ar", message = "DOC_002") String lang) {
        return ResponseEntity.ok(ApiResponse.ok(lang, documentService.getCategories(lang)));
    }
}
