package com.morocco.documentsapi.service;

import com.morocco.documentsapi.dto.CategoryListData;
import com.morocco.documentsapi.dto.DocumentDetailData;
import com.morocco.documentsapi.dto.DocumentListData;
import com.morocco.documentsapi.dto.DocumentSearchData;

public interface DocumentService {

    DocumentListData getAllDocuments(String lang);

    DocumentDetailData getDocumentByCode(String code, String lang);

    DocumentSearchData searchDocuments(String query, String lang);

    CategoryListData getCategories(String lang);
}
