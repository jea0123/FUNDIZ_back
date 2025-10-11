package com.example.funding.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.TransactionException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.sql.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;
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
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ===== 공통 유틸 ===== */
    private String getRootCauseMessage(Throwable e) {
        Throwable cause = e;
        while (cause.getCause() != null) cause = cause.getCause();
        return Optional.ofNullable(cause.getMessage()).orElse(cause.toString());
    }

    private String beforeCauseWithoutHashes(String msg) {
        if (msg == null) return null;
        String trimmed = msg.replace("###", "").trim();
        int idx = trimmed.indexOf("Cause");
        return (idx > 0) ? trimmed.substring(0, idx).trim() : trimmed;
    }

    private String clean(Throwable e) {
        return beforeCauseWithoutHashes(getRootCauseMessage(e));
    }

    private ResponseEntity<ApiError> json(HttpStatus status, String message, HttpServletRequest req) {
        return json(status, message, req, null);
    }

    private ResponseEntity<ApiError> json(HttpStatus status, String message, HttpServletRequest req, Object details) {
        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                req != null ? req.getRequestURI() : null,
                details
        );
        return ResponseEntity.status(status).body(body);
    }

    private List<FieldViolation> violations(BindException e) {
        return e.getBindingResult().getFieldErrors().stream()
                .map(this::toViolation)
                .collect(Collectors.toList());
    }

    private FieldViolation toViolation(FieldError fe) {
        String msg = Optional.ofNullable(fe.getDefaultMessage()).orElse("유효하지 않은 값");
        return new FieldViolation(fe.getField(), msg, fe.getRejectedValue());
    }

    /* =======================
       1) 클라이언트 오류 (4xx)
       ======================= */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArg(IllegalArgumentException e, HttpServletRequest req) {
        String body = "잘못된 요청입니다.";
        log.warn("[400 IllegalArgument] {} | raw: {}", body, clean(e));
        return json(HttpStatus.BAD_REQUEST, body, req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException e, HttpServletRequest req) {
        List<FieldViolation> list = violations(e);
        String body = list.isEmpty() ? "요청 값이 유효하지 않습니다." : "유효성 검증에 실패했습니다.";
        log.warn("[400 Validation] {}", list);
        return json(HttpStatus.BAD_REQUEST, body, req, list);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiError> handleBind(BindException e, HttpServletRequest req) {
        List<FieldViolation> list = violations(e);
        String body = list.isEmpty() ? "요청 바인딩에 실패했습니다." : "요청 바인딩 오류가 있습니다.";
        log.warn("[400 Bind] {}", list);
        return json(HttpStatus.BAD_REQUEST, body, req, list);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParam(MissingServletRequestParameterException e, HttpServletRequest req) {
        String body = "필수 파라미터 누락: " + e.getParameterName();
        log.warn("[400 MissingParam] {}", body);
        return json(HttpStatus.BAD_REQUEST, body, req);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException e, HttpServletRequest req) {
        Class<?> requiredType = e.getRequiredType();
        String required = requiredType != null ? requiredType.getSimpleName() : "알 수 없음";
        String body = String.format("파라미터 타입 불일치: %s (요구 타입: %s)", e.getName(), required);
        log.warn("[400 TypeMismatch] {}", body);
        return json(HttpStatus.BAD_REQUEST, body, req);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleNotReadable(HttpMessageNotReadableException e, HttpServletRequest req) {
        String body = "요청 본문을 읽을 수 없습니다. JSON 형식을 확인해주세요.";
        log.warn("[400 NotReadable] {} | raw: {}", body, clean(e));
        return json(HttpStatus.BAD_REQUEST, body, req);
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<ApiError> handleConversion(ConversionFailedException e, HttpServletRequest req) {
        String body = "값 변환에 실패했습니다.";
        log.warn("[400 Conversion] {} | raw: {}", body, clean(e));
        return json(HttpStatus.BAD_REQUEST, body, req);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiError> handleMultipart(MultipartException e, HttpServletRequest req) {
        String body = "파일 업로드 처리 중 오류가 발생했습니다.";
        log.warn("[400 Multipart] {} | raw: {}", body, clean(e));
        return json(HttpStatus.BAD_REQUEST, body, req);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuth(AuthenticationException e, HttpServletRequest req) {
        String body = "인증이 필요합니다.";
        log.warn("[401 Auth] {} | raw: {}", body, clean(e));
        return json(HttpStatus.UNAUTHORIZED, body, req);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException e, HttpServletRequest req) {
        String body = "접근 권한이 없습니다.";
        log.warn("[403 AccessDenied] {} | raw: {}", body, clean(e));
        return json(HttpStatus.FORBIDDEN, body, req);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest req) {
        String supported = Optional.ofNullable(e.getSupportedHttpMethods()).map(Object::toString).orElse("없음");
        Map<String, Object> details = Map.of("supported", supported);
        String body = String.format("허용되지 않은 HTTP 메서드입니다. 지원 메서드: %s", supported);
        log.warn("[405 MethodNotSupported] {}", body);
        return json(HttpStatus.METHOD_NOT_ALLOWED, body, req, details);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException e, HttpServletRequest req) {
        String body = "지원하지 않는 Content-Type 입니다.";
        log.warn("[415 UnsupportedMediaType] {}", body);
        return json(HttpStatus.UNSUPPORTED_MEDIA_TYPE, body, req);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ApiError> handleNotAcceptable(HttpMediaTypeNotAcceptableException e, HttpServletRequest req) {
        String body = "응답 Content-Type 협상이 실패했습니다.";
        log.warn("[406 NotAcceptable] {}", body);
        return json(HttpStatus.NOT_ACCEPTABLE, body, req);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandler(NoHandlerFoundException e, HttpServletRequest req) {
        String body = "요청하신 경로를 찾을 수 없습니다.";
        log.warn("[404 NoHandler] {} {}", e.getHttpMethod(), e.getRequestURL());
        return json(HttpStatus.NOT_FOUND, body, req);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResource(NoResourceFoundException e, HttpServletRequest req) {
        String body = "요청하신 리소스를 찾을 수 없습니다.";
        log.warn("[404 NoResource] {} | raw: {}", body, clean(e));
        return json(HttpStatus.NOT_FOUND, body, req);
    }

    /* =======================
       2) 서버 오류 (5xx)
       ======================= */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiError> handleNpe(NullPointerException e, HttpServletRequest req) {
        String body = "서버 내부 처리 중 오류가 발생했습니다.";
        log.error("[500 NPE] {} | raw: {}", body, clean(e));
        return json(HttpStatus.INTERNAL_SERVER_ERROR, body, req);
    }

    @ExceptionHandler({BeanCreationException.class, IllegalStateException.class})
    public ResponseEntity<ApiError> handleServerSide(RuntimeException e, HttpServletRequest req) {
        String body = "서버 내부 처리 중 오류가 발생했습니다.";
        log.error("[500 ServerSide] {} | raw: {}", body, clean(e));
        return json(HttpStatus.INTERNAL_SERVER_ERROR, body, req);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ApiError> handleHttpMessageConversion(HttpMessageConversionException e, HttpServletRequest req) {
        String body = "메시지 변환 중 서버 오류가 발생했습니다.";
        log.error("[500 HttpMessageConversion] {} | raw: {}", body, clean(e));
        return json(HttpStatus.INTERNAL_SERVER_ERROR, body, req);
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ApiError> handleTx(TransactionException e, HttpServletRequest req) {
        String body = "트랜잭션 처리 중 오류가 발생했습니다.";
        log.error("[500 Transaction] {} | raw: {}", body, clean(e));
        return json(HttpStatus.INTERNAL_SERVER_ERROR, body, req);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiError> handleIO(IOException e, HttpServletRequest req) {
        String body = "입출력 처리 중 오류가 발생했습니다.";
        log.error("[500 IO] {} | raw: {}", body, clean(e));
        return json(HttpStatus.INTERNAL_SERVER_ERROR, body, req);
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ResponseEntity<ApiError> handleAsyncTimeout(AsyncRequestTimeoutException e, HttpServletRequest req) {
        String body = "요청 처리 시간이 초과되었습니다.";
        log.error("[503 AsyncTimeout] {} | raw: {}", body, clean(e));
        return json(HttpStatus.SERVICE_UNAVAILABLE, body, req);
    }

    @ExceptionHandler({
            SQLSyntaxErrorException.class,
            SQLDataException.class,
            SQLFeatureNotSupportedException.class,
            BadSqlGrammarException.class
    })
    public ResponseEntity<ApiError> handleSqlSyntaxAndData(Exception e, HttpServletRequest req) {
        String body = "SQL 처리 중 오류가 발생했습니다.";
        log.error("[500 SQL] {} | raw: {}", body, clean(e));
        return json(HttpStatus.INTERNAL_SERVER_ERROR, body, req);
    }

    @ExceptionHandler({
            SQLTransientConnectionException.class,
            SQLNonTransientConnectionException.class
    })
    public ResponseEntity<ApiError> handleSqlConnection(SQLException e, HttpServletRequest req) {
        String body = "DB 연결에 문제가 발생했습니다.";
        log.error("[503 SQL-Connection] {} | raw: {}", body, clean(e));
        return json(HttpStatus.SERVICE_UNAVAILABLE, body, req);
    }

    @ExceptionHandler(BatchUpdateException.class)
    public ResponseEntity<ApiError> handleBatch(BatchUpdateException e, HttpServletRequest req) {
        String body = "배치 처리 중 오류가 발생했습니다.";
        log.error("[500 SQL-Batch] {} | raw: {}", body, clean(e));
        return json(HttpStatus.INTERNAL_SERVER_ERROR, body, req);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ApiError> handleDuplicateKey(DuplicateKeyException e, HttpServletRequest req) {
        String body = "중복된 키 값으로 인해 처리할 수 없습니다.";
        log.warn("[409 DuplicateKey] {} | raw: {}", body, clean(e));
        return json(HttpStatus.CONFLICT, body, req);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException e, HttpServletRequest req) {
        String body = "데이터 무결성 제약을 위반했습니다.";
        log.warn("[409 DataIntegrityViolation] {} | raw: {}", body, clean(e));
        return json(HttpStatus.CONFLICT, body, req);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ApiError> handleEmptyResult(EmptyResultDataAccessException e, HttpServletRequest req) {
        String body = "대상 데이터를 찾을 수 없습니다.";
        log.warn("[404 EmptyResult] {} | raw: {}", body, clean(e));
        return json(HttpStatus.NOT_FOUND, body, req);
    }

    @ExceptionHandler({
            IncorrectResultSizeDataAccessException.class,
            IncorrectUpdateSemanticsDataAccessException.class,
            ConcurrencyFailureException.class,
            OptimisticLockingFailureException.class,
            CannotAcquireLockException.class
    })
    public ResponseEntity<ApiError> handleDataSizeAndConcurrency(DataAccessException e, HttpServletRequest req) {
        String body = "동시성/결과 개수 문제로 요청을 처리할 수 없습니다.";
        log.warn("[409 DataAccess-Concurrency] {} | raw: {}", body, clean(e));
        return json(HttpStatus.CONFLICT, body, req);
    }

    @ExceptionHandler(QueryTimeoutException.class)
    public ResponseEntity<ApiError> handleQueryTimeout(QueryTimeoutException e, HttpServletRequest req) {
        String body = "DB 쿼리 처리 시간이 초과되었습니다.";
        log.error("[504 QueryTimeout] {} | raw: {}", body, clean(e));
        return json(HttpStatus.GATEWAY_TIMEOUT, body, req);
    }

    @ExceptionHandler(UncategorizedSQLException.class)
    public ResponseEntity<ApiError> handleUncategorized(UncategorizedSQLException e, HttpServletRequest req) {
        String body = "DB 처리 중 알 수 없는 오류가 발생했습니다.";
        log.error("[500 UncategorizedSQLException] {} | raw: {}", body, clean(e));
        return json(HttpStatus.INTERNAL_SERVER_ERROR, body, req);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDataAccess(DataAccessException e, HttpServletRequest req) {
        String body = "DB 처리 중 오류가 발생했습니다.";
        log.error("[500 DataAccess] {} | raw: {}", body, clean(e));
        return json(HttpStatus.INTERNAL_SERVER_ERROR, body, req);
    }

    @ExceptionHandler({
            org.apache.ibatis.exceptions.TooManyResultsException.class,
            org.mybatis.spring.MyBatisSystemException.class,
            org.apache.ibatis.exceptions.PersistenceException.class,
            org.apache.ibatis.builder.BuilderException.class,
            org.apache.ibatis.binding.BindingException.class,
            org.apache.ibatis.type.TypeException.class,
            org.apache.ibatis.executor.ExecutorException.class,
            org.apache.ibatis.executor.result.ResultMapException.class,
            org.apache.ibatis.reflection.ReflectionException.class,
            org.apache.ibatis.scripting.ScriptingException.class,
            org.apache.ibatis.plugin.PluginException.class,
            org.apache.ibatis.cache.CacheException.class,
            org.apache.ibatis.session.SqlSessionException.class
    })
    public ResponseEntity<ApiError> handleMyBatisCore(Exception e, HttpServletRequest req) {
        String raw = clean(e);
        if (e instanceof org.apache.ibatis.exceptions.TooManyResultsException) {
            String body = "단건 조회에 여러 행이 반환되었습니다.";
            log.warn("[409 MyBatis TooManyResults] {} | raw: {}", body, raw);
            return json(HttpStatus.CONFLICT, body, req);
        }
        String body = "MyBatis 처리 중 오류가 발생했습니다.";
        log.error("[500 MyBatis] {} | raw: {}", body, raw);
        return json(HttpStatus.INTERNAL_SERVER_ERROR, body, req);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiError> handleMaxUpload(MaxUploadSizeExceededException e, HttpServletRequest req) {
        String body = "업로드 용량 제한을 초과했습니다.";
        log.warn("[413 PayloadTooLarge] {}", body);
        return json(HttpStatus.PAYLOAD_TOO_LARGE, body, req);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ApiError> handleResourceAccess(ResourceAccessException e, HttpServletRequest req) {
        String body = "외부 서비스와의 통신에 실패했습니다.";
        log.error("[504 ResourceAccess] {} | raw: {}", body, clean(e));
        return json(HttpStatus.GATEWAY_TIMEOUT, body, req);
    }

    @ExceptionHandler(UserPrincipalNotFoundException.class)
    public ResponseEntity<ApiError> handleUserPrincipalNotFound(UserPrincipalNotFoundException e, HttpServletRequest req) {
        String body = "사용자 정보를 찾을 수 없습니다.";
        log.warn("[404 UserPrincipalNotFound] {} | raw: {}", body, clean(e));
        return json(HttpStatus.NOT_FOUND, body, req);
    }

    /* ========= ResponseStatusException 패스스루 ========= */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatus(ResponseStatusException e, HttpServletRequest req) {
        String body = e.getReason() != null ? e.getReason() : "오류가 발생했습니다.";
        HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
        if (status.is4xxClientError()) log.warn("[ResponseStatus {}] {}", status.value(), body);
        else log.error("[ResponseStatus {}] {} | raw: {}", status.value(), body, clean(e));
        return json(status, body, req);
    }

    /* ========= 마지막 안전망 ========= */
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiError> handleUnknown(Exception e, HttpServletRequest req) {
//        String body = "일시적인 오류가 발생했습니다.";
//        log.error("[500 Unknown] {} | raw: {}", body, clean(e));
//        return json(HttpStatus.INTERNAL_SERVER_ERROR, body, req);
//    }

    /* ===== 공통 응답 모델 ===== */
    public record ApiError(
            Instant timestamp,
            int status,
            String error,
            String message,
            String path,
            Object details // 검증 오류 목록 등 부가정보(없으면 null)
    ) {
    }

    public record FieldViolation(String field, String message, Object rejectedValue) {
    }
}
