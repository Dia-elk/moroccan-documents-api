package com.morocco.documentsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private String name;
    private String address;
    private String phone;
    private String email;
    private String workingHours;
}
