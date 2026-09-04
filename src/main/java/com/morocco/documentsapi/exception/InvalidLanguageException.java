package com.morocco.documentsapi.exception;

public class InvalidLanguageException extends DocumentsApiException {

    public InvalidLanguageException() {
        super(ErrorCode.DOC_002);
    }
}
