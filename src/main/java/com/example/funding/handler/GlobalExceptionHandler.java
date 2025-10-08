package com.example.funding.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.TransactionException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.sql.SQLSyntaxErrorException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>글로벌 예외 처리 핸들러</p>
 * <p>- 애플리케이션 전역에서 발생하는 다양한 예외를 처리하고 적절한 HTTP 응답을 반환</p>
 *
 * @author 장민규
 * @since 2025-08-26
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 루트 원인 메시지 추출
     */
    private String getRootCauseMessage(Throwable e) {
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return Optional.ofNullable(cause.getMessage()).orElse(cause.toString());
    }

    /**
     * "###" 제거 + "Cause" 이전까지만 남김 (예: JDBC/MyBatis 오류 포맷 정리)
     */
    private String beforeCauseWithoutHashes(String msg) {
        if (msg == null) return null;
        String trimmed = msg.replace("###", "").trim();
        int idx = trimmed.indexOf("Cause");
        return (idx > 0) ? trimmed.substring(0, idx).trim() : trimmed;
    }

    // ============ 개별 예외별 한글 메시지 핸들러 ============

    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleBadRequest(RuntimeException e) {
        String clean = beforeCauseWithoutHashes(getRootCauseMessage(e));
        String body = "잘못된 요청입니다.";
        log.warn("[BadRequest] {} | raw: {}", body, clean);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler({SQLSyntaxErrorException.class})
    public ResponseEntity<String> handleSqlSyntax(SQLSyntaxErrorException e) {
        String clean = beforeCauseWithoutHashes(getRootCauseMessage(e));
        String body = "SQL 구문 오류가 발생했습니다.";
        log.error("[SQLSyntaxError] {} | raw: {}", body, clean);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccess(DataAccessException e) {
        String clean = beforeCauseWithoutHashes(getRootCauseMessage(e));
        String body = "DB 처리 중 오류가 발생했습니다.";
        log.error("[DataAccess] {} | raw: {}", body, clean);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException e) {
        String details = e.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining(", "));
        String body = details.isBlank() ? "요청 값이 유효하지 않습니다." : details;
        log.warn("[Validation] {}", body);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> handleBind(BindException e) {
        String details = e.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining(", "));
        String body = details.isBlank() ? "요청 바인딩에 실패했습니다." : details;
        log.warn("[Bind] {}", body);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    private String formatFieldError(FieldError fe) {
        String field = fe.getField();
        String msg = Optional.ofNullable(fe.getDefaultMessage()).orElse("유효하지 않은 값");
        Object rejected = fe.getRejectedValue();
        return String.format("%s: %s%s",
                field, msg, rejected == null ? "" : " (입력값: " + rejected + ")");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParam(MissingServletRequestParameterException e) {
        String body = "필수 파라미터 누락: " + e.getParameterName();
        log.warn("[MissingParam] {}", body);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String required = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "알 수 없음";
        String body = String.format("파라미터 타입 불일치: %s (요구 타입: %s)", e.getName(), required);
        log.warn("[TypeMismatch] {}", body);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleNotReadable(HttpMessageNotReadableException e) {
        String body = "요청 본문을 읽을 수 없습니다. JSON 형식을 확인해주세요.";
        log.warn("[NotReadable] {}", body);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        String supported = (e.getSupportedHttpMethods() == null || e.getSupportedHttpMethods().isEmpty())
                ? "없음" : e.getSupportedHttpMethods().toString();
        String body = String.format("허용되지 않은 HTTP 메서드입니다. 지원 메서드: %s", supported);
        log.warn("[MethodNotSupported] {}", body);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(body);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandler(NoHandlerFoundException e) {
        String body = "요청하신 경로를 찾을 수 없습니다.";
        log.warn("[NoHandler] {}", body);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUpload(MaxUploadSizeExceededException e) {
        String body = "업로드 용량 제한을 초과했습니다.";
        log.warn("[MaxUpload] {}", body);
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(body);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<String> handleMultipart(MultipartException e) {
        String body = "파일 업로드 처리 중 오류가 발생했습니다.";
        log.warn("[Multipart] {}", body);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuth(AuthenticationException e) {
        String body = "인증이 필요합니다.";
        log.warn("[Auth] {}", body);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException e) {
        String body = "접근 권한이 없습니다.";
        log.warn("[AccessDenied] {}", body);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<String> handleConversion(ConversionFailedException e) {
        String body = "값 변환에 실패했습니다.";
        log.warn("[Conversion] {}", body);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<String> handleTx(TransactionException e) {
        String body = "트랜잭션 처리 중 오류가 발생했습니다.";
        log.error("[Transaction] {}", body, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler({BeanCreationException.class, JsonProcessingException.class, IllegalStateException.class})
    public ResponseEntity<String> handleServerSide(RuntimeException e) {
        String body = "서버 내부 처리 중 오류가 발생했습니다.";
        log.error("[ServerSide] {}", body, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatus(ResponseStatusException e) {
        String body = e.getReason() != null ? e.getReason() : "오류가 발생했습니다.";
        HttpStatusCode status = e.getStatusCode();
        if (status.is4xxClientError()) {
            log.warn("[ResponseStatus] {} | {}", status.value(), body);
        } else {
            log.error("[ResponseStatus] {} | {}", status.value(), body, e);
        }
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIO(IOException e) {
        String body = "입출력 처리 중 오류가 발생했습니다.";
        log.error("[IO] {}", body, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    // ========= 마지막 안전망 =========
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnknown(Exception e) {
        String body = "일시적인 오류가 발생했습니다.";
        log.error("[Unknown] {}", body, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
