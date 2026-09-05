package com.morocco.documentsapi.exception;

import lombok.Getter;

@Getter
public class DocumentsApiException extends RuntimeException {

    private final ErrorCode errorCode;

    public DocumentsApiException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }
}
