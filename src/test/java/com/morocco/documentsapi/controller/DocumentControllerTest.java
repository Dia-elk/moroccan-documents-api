package com.morocco.documentsapi.controller;

import com.morocco.documentsapi.dto.DocumentDetailData;
import com.morocco.documentsapi.dto.DocumentDto;
import com.morocco.documentsapi.dto.DocumentListData;
import com.morocco.documentsapi.dto.DocumentSearchData;
import com.morocco.documentsapi.exception.DocumentNotFoundException;
import com.morocco.documentsapi.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentController.class)
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    @Test
    void getAllDocuments_defaultLanguage_returnsFrenchOk() throws Exception {
        DocumentDto dto = DocumentDto.builder().code("national-id").name("Carte Nationale").build();
        when(documentService.getAllDocuments("fr")).thenReturn(
                DocumentListData.builder().count(1).documents(List.of(dto)).build());

        mockMvc.perform(get("/api/v1/documents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.language").value("fr"))
                .andExpect(jsonPath("$.data.documents[0].code").value("national-id"));
    }

    @Test
    void getAllDocuments_arabicLanguage_returnsOk() throws Exception {
        when(documentService.getAllDocuments("ar")).thenReturn(
                DocumentListData.builder().count(0).documents(List.of()).build());

        mockMvc.perform(get("/api/v1/documents").param("lang", "ar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.language").value("ar"));
    }

    @Test
    void getAllDocuments_invalidLanguage_returnsBadRequestWithErrorCode() throws Exception {
        mockMvc.perform(get("/api/v1/documents").param("lang", "es"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("DOC_002"));
    }

    @Test
    void getDocumentByCode_found_returnsOk() throws Exception {
        DocumentDto dto = DocumentDto.builder().code("passport").name("Passeport").build();
        when(documentService.getDocumentByCode("passport", "fr")).thenReturn(
                DocumentDetailData.builder()
                        .document(dto)
                        .requirements(List.of())
                        .procedures(List.of())
                        .locations(List.of())
                        .build());

        mockMvc.perform(get("/api/v1/documents/passport"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.document.code").value("passport"));
    }

    @Test
    void getDocumentByCode_notFound_returns404WithErrorCode() throws Exception {
        when(documentService.getDocumentByCode("unknown", "fr")).thenThrow(new DocumentNotFoundException());

        mockMvc.perform(get("/api/v1/documents/unknown"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("DOC_001"))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void searchDocuments_missingQuery_returnsBadRequestWithErrorCode() throws Exception {
        mockMvc.perform(get("/api/v1/documents/search"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("DOC_003"));
    }

    @Test
    void searchDocuments_withQuery_returnsOk() throws Exception {
        when(documentService.searchDocuments("passeport", "fr")).thenReturn(
                DocumentSearchData.builder().query("passeport").count(0).documents(List.of()).build());

        mockMvc.perform(get("/api/v1/documents/search").param("query", "passeport"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.query").value("passeport"));
    }
}
