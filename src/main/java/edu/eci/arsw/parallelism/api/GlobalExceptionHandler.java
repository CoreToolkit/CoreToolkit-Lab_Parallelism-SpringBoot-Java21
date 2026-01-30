
package edu.eci.arsw.parallelism.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the REST API.
 * Catches exceptions thrown by controllers and converts them into proper HTTP
 * responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles custom PiCalculationException with centralized error messages.
     * Returns 400 Bad Request with the controlled error message.
     */
    @ExceptionHandler(PiCalculationException.class)
    public ResponseEntity<Map<String, Object>> handlePiCalculationException(
            PiCalculationException ex,
            WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Handles type mismatch errors (e.g., passing "abc" when an int is expected).
     * Returns 400 Bad Request.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", PiCalculationException.INVALID_PARAMETER_TYPE + " for parameter '" + ex.getName() + "'");

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Handles any other unexpected exceptions.
     * Returns 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex,
            WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected error occurred");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    /**
     * Handles MissingServletRequestParameterException (missing required parameters)
     * This happens when a required @RequestParam is missing from the request
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)  // ← NUEVO
    public ResponseEntity<Map<String, Object>> handleMissingParameter(
            MissingServletRequestParameterException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", "Required parameter '" + ex.getParameterName() + "' is missing");

        return ResponseEntity.badRequest().body(body);
    }
}
