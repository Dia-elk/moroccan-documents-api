package com.morocco.documentsapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Generic envelope used by every endpoint in this API so that success and error
 * responses always share the same top-level shape: success flag, requested
 * language, payload, and (on errors) a client-facing error code.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String language;
    private T data;

    /** Set only on error responses. See the error code table in README. */
    private String code;

    /** Localized message. Human-readable summary on success, localized error text on failure. */
    private String message;

    @Builder.Default
    private Instant timestamp = Instant.now();

    public static <T> ApiResponse<T> ok(String language, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .language(language)
                .data(data)
                .build();
    }
}
