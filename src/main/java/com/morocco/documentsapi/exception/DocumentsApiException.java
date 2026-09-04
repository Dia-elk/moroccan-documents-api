package com.morocco.documentsapi.exception;

import lombok.Getter;

/**
 * Base exception for this service. The exception message is always the error
 * code — never a human readable string. Human readable text is resolved from
 * {@link ErrorCode#getMessage(String)} only at the point the HTTP response is built.
 */
@Getter
public class DocumentsApiException extends RuntimeException {

    private final ErrorCode errorCode;

    public DocumentsApiException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }
}
