package com.morocco.documentsapi.exception;

import com.morocco.documentsapi.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(DocumentNotFoundException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getErrorCode(), request);
    }

    @ExceptionHandler({
            InvalidLanguageException.class,
            InvalidSearchQueryException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleDomainBadRequest(DocumentsApiException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, ex.getErrorCode(), request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, ErrorCode.DOC_002, request);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        ErrorCode code = "query".equals(ex.getParameterName()) ? ErrorCode.DOC_003 : ErrorCode.DOC_004;
        return build(HttpStatus.BAD_REQUEST, code, request);
    }

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ApiResponse<Void>> handleNotFoundRoute(HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ErrorCode.DOC_004, request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotAllowed(HttpServletRequest request) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, ErrorCode.DOC_004, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error handling {} {}", request.getMethod(), request.getRequestURI(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.DOC_000, request);
    }

    private ResponseEntity<ApiResponse<Void>> build(HttpStatus status, ErrorCode code, HttpServletRequest request) {
        String lang = resolveLang(request);
        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .success(false)
                .language(lang)
                .code(code.name())
                .message(code.getMessage(lang))
                .build();
        return ResponseEntity.status(status).body(body);
    }

    private String resolveLang(HttpServletRequest request) {
        String lang = request.getParameter("lang");
        return ("ar".equals(lang) || "fr".equals(lang)) ? lang : "fr";
    }
}
