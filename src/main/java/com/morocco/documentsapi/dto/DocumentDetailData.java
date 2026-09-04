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
public class DocumentDetailData {
    private DocumentDto document;
    private List<RequirementDto> requirements;
    private List<ProcedureDto> procedures;
    private List<LocationDto> locations;
}
