package com.morocco.documentsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentSearchData {
    private String query;
    private int count;
    private List<DocumentDto> documents;
}
