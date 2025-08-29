package com.example.funding.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * <p>JWT 인증 실패 핸들러</p>
 * <p>- 인증이 필요한 리소스에 인증 없이 접근할 경우 401 Unauthorized 응답을 반환</p>
 * @since 2025-08-26
 * @author 장민규
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * <p>인증 실패 시 호출되는 메서드</p>
     * <p>- response에 401 상태 코드와 에러 메시지를 JSON 형식으로 작성</p>
     * @param request  HTTP 요청
     * @param response HTTP 응답
     * @param authException 발생한 인증 예외
     * @throws IOException      입출력 예외
     * @throws ServletException 서블릿 예외
     * @since 2025-08-26
     * @author 장민규
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        if (response.isCommitted()) return; // 이미 커밋됐으면 중단

        String code = (String) request.getAttribute("authError");
        int status = HttpServletResponse.SC_UNAUTHORIZED;
        String message = "인증이 필요합니다.";
        if ("TOKEN_EXPIRED".equals(code)) message = "토큰이 만료되었습니다.";
        else if ("TOKEN_INVALID".equals(code)) message = "유효하지 않은 토큰입니다.";
        else if ("TOKEN_MISSING".equals(code)) message = "토큰이 없습니다.";
        else if ("AUTHENTICATION_FAILED".equals(code)) message = "인증에 실패했습니다.";
        else if ("ACCESS_DENIED".equals(code)) {
            status = HttpServletResponse.SC_FORBIDDEN;
            message = "접근이 거부되었습니다.";
        } else if (code != null) {
            status = HttpServletResponse.SC_BAD_REQUEST;
            message = "잘못된 요청입니다: " + code;
        }

        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");

        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        body.put("status", status);
        body.put("message", message);
        body.put("path", request.getRequestURI());
        body.put("timestamp", java.time.Instant.now().toString());

        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }
}
