package rycfod.com.the_tracking_guard_dog.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Centralises error handling for all controllers (Single Responsibility).
 * Each handler maps one exception type → one HTTP status + ErrorResponse body.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** ShipEngine returned a 4xx or 5xx — forward the status to our caller. */
    @ExceptionHandler(ShipEngineApiException.class)
    public ResponseEntity<ErrorResponse> handleShipEngineApi(
            ShipEngineApiException ex, HttpServletRequest req) {

        log.error("[ShipEngine] API error {} — {}", ex.getHttpStatus(), ex.getMessage());

        int statusCode = ex.getHttpStatus().value();
        ErrorResponse body = ErrorResponse.builder()
                .status(statusCode)
                .error("ShipEngine API Error")
                .message(ex.getMessage())
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(statusCode).body(body);
    }

    /** Bean-validation failures on incoming request DTOs. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest req) {

        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .reduce("", (a, b) -> a.isEmpty() ? b : a + "; " + b);

        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message(details)
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.badRequest().body(body);
    }

    /** 404 — static resource not found (e.g. favicon.ico, browser probes). No stack trace needed. */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResource(NoResourceFoundException ex) {
        log.debug("[404] {}", ex.getMessage());
        return ResponseEntity.notFound().build();
    }

    /** Catch-all for anything unexpected. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex, HttpServletRequest req) {

        log.error("[Unhandled] {}: {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);

        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred. Check server logs.")
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.internalServerError().body(body);
    }
}
