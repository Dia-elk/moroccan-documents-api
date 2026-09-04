package com.morocco.documentsapi.exception;

public class DocumentNotFoundException extends DocumentsApiException {

    public DocumentNotFoundException() {
        super(ErrorCode.DOC_001);
    }
}
