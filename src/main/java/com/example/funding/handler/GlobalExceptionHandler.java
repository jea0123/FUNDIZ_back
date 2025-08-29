package com.example.funding.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.TransactionException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Optional;

/**
 * <p>글로벌 예외 처리 핸들러</p>
 * <p>- 애플리케이션 전역에서 발생하는 다양한 예외를 처리하고 적절한 HTTP 응답을 반환</p>
 * @since 2025-08-26
 * @author 장민규
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private String getRootCauseMessage(Throwable e) {
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return Optional.ofNullable(cause.getMessage()).orElse(cause.toString());
    }

    /**
     * <p>일반적인 예외 처리 메서드</p>
     * <p>- 다양한 예외를 포괄적으로 처리하고, 400 Bad Request 응답을 반환</p>
     * @param e 발생한 예외
     * @return 400 Bad Request 응답과 예외 메시지
     * @since 2025-08-26
     * @author 장민규
     */
    @ExceptionHandler({
            NullPointerException.class,
            IllegalArgumentException.class,
            IllegalStateException.class,
            MethodArgumentNotValidException.class,
            BindException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class,
            HttpRequestMethodNotSupportedException.class,
            NoHandlerFoundException.class,
            MaxUploadSizeExceededException.class,
            MultipartException.class,
            ConversionFailedException.class,
            TransactionException.class,
            AuthenticationException.class,
            AccessDeniedException.class,
            DataAccessException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMessageNotReadableException.class,
            JsonProcessingException.class,
            BeanCreationException.class
    })
    public ResponseEntity<String> handleAll(Exception e) {
        String msg = getRootCauseMessage(e);
        log.error("예외 발생: {}", Optional.ofNullable(e.getMessage()).orElse(e.toString()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(msg);
    }

    /**
     * <p>알 수 없는 예외 처리 메서드</p>
     * <p>- 처리되지 않은 예외를 포괄적으로 처리하고, 500 Internal Server Error 응답을 반환</p>
     * @param e 발생한 예외
     * @return 500 Internal Server Error 응답과 일반적인 오류 메시지
     * @since 2025-08-27
     * @author 장민규
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnknown(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("일시적인 오류가 발생했습니다.");
    }
}
