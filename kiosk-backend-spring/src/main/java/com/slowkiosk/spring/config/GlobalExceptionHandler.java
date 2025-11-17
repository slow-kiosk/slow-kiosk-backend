package com.slowkiosk.spring.config; // (또는 com.slowkiosk.spring.exception)

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice // 모든 @RestController에서 발생하는 예외를 가로챕니다.
public class GlobalExceptionHandler {

    /**
     * EntityNotFoundException 처리 (e.g., 메뉴를 찾지 못함)
     * MenuService, OrderService, OrchestrationService에서 발생
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("Entity Not Found: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404 응답
                .body(Map.of("status", "404", "error", e.getMessage()));
    }

    /**
     * (선택)IllegalArgumentException 처리 (e.g., 잘못된 요청 값)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Bad Request: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400 응답
                .body(Map.of("status", "400", "error", e.getMessage()));
    }

    /**
     * 모든 그 외의 예외 처리 (e.g., NullPointerException)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception e) {
        log.error("Unhandled Exception: ", e); // 심각한 에러는 stack trace를 로깅
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 응답
                .body(Map.of("status", "500", "error", "서버 내부 오류가 발생했습니다."));
    }
}