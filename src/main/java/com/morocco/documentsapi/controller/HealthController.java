package com.morocco.documentsapi.controller;

import com.morocco.documentsapi.dto.ApiResponse;
import com.morocco.documentsapi.dto.HealthData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<ApiResponse<HealthData>> health() {
        HealthData data = HealthData.builder()
                .status("UP")
                .service("moroccan-documents-api")
                .build();
        return ResponseEntity.ok(ApiResponse.ok("fr", data));
    }
}
