package com.morocco.documentsapi.exception;

public class InvalidSearchQueryException extends DocumentsApiException {

    public InvalidSearchQueryException() {
        super(ErrorCode.DOC_003);
    }
}
