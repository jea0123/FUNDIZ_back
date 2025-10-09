package com.example.funding.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.result.ResultMapException;
import org.apache.ibatis.plugin.PluginException;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.scripting.ScriptingException;
import org.apache.ibatis.session.SqlSessionException;
import org.apache.ibatis.type.TypeException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

import java.io.IOException;
import java.sql.*;
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

    /* =======================
       1) 클라이언트 오류 (4xx)
       ======================= */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArg(IllegalArgumentException e) {
        String body = "잘못된 요청입니다.";
        log.warn("[400 IllegalArgument] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException e) {
        String details = e.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError).collect(Collectors.joining(", "));
        String body = details.isBlank() ? "요청 값이 유효하지 않습니다." : details;
        log.warn("[400 Validation] {}", body);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> handleBind(BindException e) {
        String details = e.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError).collect(Collectors.joining(", "));
        String body = details.isBlank() ? "요청 바인딩에 실패했습니다." : details;
        log.warn("[400 Bind] {}", body);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParam(MissingServletRequestParameterException e) {
        String body = "필수 파라미터 누락: " + e.getParameterName();
        log.warn("[400 MissingParam] {}", body);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        Class<?> requiredType = e.getRequiredType();
        String required = requiredType != null ? requiredType.getSimpleName() : "알 수 없음";
        String body = String.format("파라미터 타입 불일치: %s (요구 타입: %s)", e.getName(), required);
        log.warn("[400 TypeMismatch] {}", body);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleNotReadable(HttpMessageNotReadableException e) {
        String body = "요청 본문을 읽을 수 없습니다. JSON 형식을 확인해주세요.";
        log.warn("[400 NotReadable] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<String> handleConversion(ConversionFailedException e) {
        String body = "값 변환에 실패했습니다.";
        log.warn("[400 Conversion] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler({MultipartException.class})
    public ResponseEntity<String> handleMultipart(MultipartException e) {
        String body = "파일 업로드 처리 중 오류가 발생했습니다.";
        log.warn("[400 Multipart] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuth(AuthenticationException e) {
        String body = "인증이 필요합니다.";
        log.warn("[401 Auth] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException e) {
        String body = "접근 권한이 없습니다.";
        log.warn("[403 AccessDenied] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        String supported = Optional.ofNullable(e.getSupportedHttpMethods())
                .map(Object::toString).orElse("없음");
        String body = String.format("허용되지 않은 HTTP 메서드입니다. 지원 메서드: %s", supported);
        log.warn("[405 MethodNotSupported] {}", body);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(body);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException e) {
        String body = "지원하지 않는 Content-Type 입니다.";
        log.warn("[415 UnsupportedMediaType] {}", body);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(body);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<String> handleNotAcceptable(HttpMediaTypeNotAcceptableException e) {
        String body = "응답 Content-Type 협상이 실패했습니다.";
        log.warn("[406 NotAcceptable] {}", body);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(body);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandler(NoHandlerFoundException e) {
        String body = "요청하신 경로를 찾을 수 없습니다.";
        log.warn("[404 NoHandler] {} {}", e.getHttpMethod(), e.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /* =======================
       2) 서버 오류 (5xx)
       ======================= */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNpe(NullPointerException e) {
        String body = "서버 내부 처리 중 오류가 발생했습니다.";
        log.error("[500 NPE] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler({BeanCreationException.class, IllegalStateException.class})
    public ResponseEntity<String> handleServerSide(RuntimeException e) {
        String body = "서버 내부 처리 중 오류가 발생했습니다.";
        log.error("[500 ServerSide] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<String> handleHttpMessageConversion(HttpMessageConversionException e) {
        String body = "메시지 변환 중 서버 오류가 발생했습니다.";
        log.error("[500 HttpMessageConversion] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    // 트랜잭션/IO/비동기 타임아웃
    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<String> handleTx(TransactionException e) {
        String body = "트랜잭션 처리 중 오류가 발생했습니다.";
        log.error("[500 Transaction] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIO(IOException e) {
        String body = "입출력 처리 중 오류가 발생했습니다.";
        log.error("[500 IO] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ResponseEntity<String> handleAsyncTimeout(AsyncRequestTimeoutException e) {
        String body = "요청 처리 시간이 초과되었습니다.";
        log.error("[503 AsyncTimeout] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }

    @ExceptionHandler({
            SQLSyntaxErrorException.class, // 문법 오류
            SQLDataException.class,        // 데이터 변환/범위 문제
            SQLFeatureNotSupportedException.class
    })
    public ResponseEntity<String> handleSqlSyntaxAndData(SQLException e) {
        String body = "SQL 처리 중 오류가 발생했습니다.";
        log.error("[500 SQL] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler({
            SQLTransientConnectionException.class,
            SQLNonTransientConnectionException.class
    })
    public ResponseEntity<String> handleSqlConnection(SQLException e) {
        String body = "DB 연결에 문제가 발생했습니다.";
        log.error("[503 SQL-Connection] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }

    @ExceptionHandler({BatchUpdateException.class})
    public ResponseEntity<String> handleBatch(BatchUpdateException e) {
        String body = "배치 처리 중 오류가 발생했습니다.";
        log.error("[500 SQL-Batch] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<String> handleDuplicateKey(DuplicateKeyException e) {
        String body = "중복된 키 값으로 인해 처리할 수 없습니다.";
        log.warn("[409 DuplicateKey] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrity(DataIntegrityViolationException e) {
        String body = "데이터 무결성 제약을 위반했습니다.";
        log.warn("[409 DataIntegrityViolation] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<String> handleEmptyResult(EmptyResultDataAccessException e) {
        String body = "대상 데이터를 찾을 수 없습니다.";
        log.warn("[404 EmptyResult] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler({
            IncorrectResultSizeDataAccessException.class,
            IncorrectUpdateSemanticsDataAccessException.class,
            ConcurrencyFailureException.class,
            OptimisticLockingFailureException.class,
            CannotAcquireLockException.class
    })
    public ResponseEntity<String> handleDataSizeAndConcurrency(DataAccessException e) {
        String body = "동시성/결과 개수 문제로 요청을 처리할 수 없습니다.";
        log.warn("[409 DataAccess-Concurrency] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(QueryTimeoutException.class)
    public ResponseEntity<String> handleQueryTimeout(QueryTimeoutException e) {
        String body = "DB 쿼리 처리 시간이 초과되었습니다.";
        log.error("[504 QueryTimeout] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(body);
    }

    @ExceptionHandler(UncategorizedSQLException.class)
    public ResponseEntity<String> handleUncategorized(UncategorizedSQLException e) {
        String body = "DB 처리 중 알 수 없는 오류가 발생했습니다.";
        log.error("[500 UncategorizedSQLException] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccess(DataAccessException e) {
        String body = "DB 처리 중 오류가 발생했습니다.";
        log.error("[500 DataAccess] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler({
            MyBatisSystemException.class,
            PersistenceException.class,
            BuilderException.class,
            BindingException.class,
            TypeException.class,
            ExecutorException.class,
            ResultMapException.class,
            ReflectionException.class,
            ScriptingException.class,
            PluginException.class,
            CacheException.class,
            SqlSessionException.class
    })
    public ResponseEntity<String> handleMyBatisCore(Exception e) {
        String raw = clean(e);
        if (e instanceof TooManyResultsException) {
            String body = "단건 조회에 여러 행이 반환되었습니다.";
            log.warn("[409 MyBatis TooManyResults] {} | raw: {}", body, raw);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }
        String body = "MyBatis 처리 중 오류가 발생했습니다.";
        log.error("[500 MyBatis] {} | raw: {}", body, raw);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    // 업로드 용량
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUpload(MaxUploadSizeExceededException e) {
        String body = "업로드 용량 제한을 초과했습니다.";
        log.warn("[413 PayloadTooLarge] {}", body);
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(body);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<String> handleResourceAccess(ResourceAccessException e) {
        String body = "외부 서비스와의 통신에 실패했습니다.";
        log.error("[504 ResourceAccess] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(body);
    }

    /* ========= ResponseStatusException 패스스루 ========= */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatus(ResponseStatusException e) {
        String body = e.getReason() != null ? e.getReason() : "오류가 발생했습니다.";
        HttpStatusCode status = e.getStatusCode();
        if (status.is4xxClientError()) log.warn("[ResponseStatus {}] {}", status.value(), body);
        else log.error("[ResponseStatus {}] {} | raw: {}", status.value(), body, clean(e));
        return ResponseEntity.status(status).body(body);
    }

    /* ========= 마지막 안전망 ========= */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnknown(Exception e) {
        String body = "일시적인 오류가 발생했습니다.";
        log.error("[500 Unknown] {} | raw: {}", body, clean(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    /* ===== 유틸: 필드 에러 포맷 ===== */
    private String formatFieldError(FieldError fe) {
        String field = fe.getField();
        String msg = Optional.ofNullable(fe.getDefaultMessage()).orElse("유효하지 않은 값");
        Object rejected = fe.getRejectedValue();
        return String.format("%s: %s%s", field, msg, rejected == null ? "" : " (입력값: " + rejected + ")");
    }
}
