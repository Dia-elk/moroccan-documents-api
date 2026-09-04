package com.morocco.documentsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
    private String code;
    private String name;
    private String description;
    private String category;
    private String categoryLabel;
    private BigDecimal feeAmount;
    private String feeCurrency;
    private boolean feeVariable;
    private String feeNote;
    private Integer processingDays;
    private String location;
}
