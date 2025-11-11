package com.example.userservice.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ProblemDetailResponse> handleCustomException(
            CustomException ex, ServerHttpRequest request) {

        HttpStatus status = switch (ex.getCode()) {
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case BAD_REQUEST -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return ResponseEntity.status(status).body(new ProblemDetailResponse(
                status.value(),
                ex.getCode().name(),
                ex.getMessage(),
                request.getPath().value()
        ));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ProblemDetailResponse> handleBindException(
            WebExchangeBindException ex, ServerHttpRequest request) {

        String detail = ex.getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ProblemDetailResponse(
                HttpStatus.BAD_REQUEST.value(),
                "BAD_REQUEST",
                detail.isEmpty() ? ex.getMessage() : detail,
                request.getPath().value()
        ));
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ProblemDetailResponse> handleInputException(
            ServerWebInputException ex, ServerHttpRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ProblemDetailResponse(
                HttpStatus.BAD_REQUEST.value(),
                "BAD_REQUEST",
                ex.getReason() != null ? ex.getReason() : ex.getMessage(),
                request.getPath().value()
        ));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetailResponse> handleResponseStatus(
            ResponseStatusException ex, ServerHttpRequest request) {

        HttpStatus status = ex.getStatusCode() instanceof HttpStatus http ? http : HttpStatus.INTERNAL_SERVER_ERROR;
        String title = status == HttpStatus.NOT_FOUND ? "NOT_FOUND" : status.name();

        return ResponseEntity.status(status).body(new ProblemDetailResponse(
                status.value(),
                title,
                ex.getReason() != null ? ex.getReason() : "Resource not found",
                request.getPath().value()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetailResponse> handleGeneralException(
            Exception ex, ServerHttpRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ProblemDetailResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_ERROR",
                ex.getMessage(),
                request.getPath().value()
        ));
    }

    public record ProblemDetailResponse(
            int status,
            String title,
            String detail,
            String instance
    ) {}
}
