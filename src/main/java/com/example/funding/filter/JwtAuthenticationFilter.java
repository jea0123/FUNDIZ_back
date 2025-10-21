package com.example.funding.filter;

import com.example.funding.common.CustomUserPrincipal;
import com.example.funding.mapper.UserMapper;
import com.example.funding.provider.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>JWT 인증 필터</p>
 * <p>- 매 요청마다 JWT 토큰의 유효성을 검사하고, 유효한 경우 SecurityContext에 인증 정보를 저장</p>
 *
 * @author 장민규
 * @since 2028-08-26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String[] WHITELIST = {
            "/api/v1/auth/**",
            "/public/**", "/swagger-ui/**", "/v3/api-docs/**",
            "/error", "/favicon.ico",
    };
    private final JwtAuthenticationEntryPoint entryPoint;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;

    /**
     * <p>필터를 적용하지 않을 경로 설정</p>
     * <p>- OPTIONS 메서드 요청과 WHITELIST에 포함된 경로는 필터를 적용하지 않음</p>
     */
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest req) {
//        if ("OPTIONS".equalsIgnoreCase(req.getMethod()))
//            return true;
        String path = req.getRequestURI();
        for (String p : WHITELIST)
            if (pathMatcher.match(p, path))
                return true;
        return false;
    }

    /**
     * <p>JWT 토큰 검증 및 인증 정보 설정</p>
     * <p>- 요청 헤더에서 JWT 토큰을 추출하고, 유효성을 검사</p>
     * <p>- 토큰이 유효한 경우, 토큰에서 사용자 정보를 추출하여 SecurityContext에 인증 정보 설정</p>
     * <p>- 토큰이 없거나 유효하지 않은 경우, 인증 실패 처리</p>
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        //TODO: CORS 프리플라이트 무조건 통과 (임시)
//        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        String token = resolveToken(request);
        if (token == null || token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = jwtProvider.validateAndGetClaims(token);
            String role = claims.get("role", String.class);
            List<GrantedAuthority> auths = new ArrayList<>();
            auths.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
            if("ADMIN".equalsIgnoreCase(role)) {
                //관리자는 여기서 끝
                CustomUserPrincipal principal = new CustomUserPrincipal(null, null, claims.getSubject(), auths);
                Authentication auth =
                        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);

                filterChain.doFilter(request, response);
                return;
            }
            Long userId = ((Number) claims.get("userId")).longValue();
            Long creatorId = userMapper.getCreatorIdByUserId(userId);
            if (creatorId != null) {
                auths.add(new SimpleGrantedAuthority("ROLE_CREATOR"));
            }

            String email = claims.get("email", String.class);
            if (email == null || userMapper.findByEmail(email) == null) {
                request.setAttribute("authError", "TOKEN_INVALID");
                entryPoint.commence(request, response, null);
                return;
            }

            CustomUserPrincipal principal = new CustomUserPrincipal(userId, creatorId, email, auths);
            Authentication auth =
                    new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            request.setAttribute("authError", "TOKEN_EXPIRED");
            entryPoint.commence(request, response, null);
        } catch (JwtException | IllegalArgumentException e) {
            request.setAttribute("authError", "TOKEN_INVALID");
            entryPoint.commence(request, response, null);
        } catch (SecurityException e) {
            request.setAttribute("authError", "ACCESS_DENIED");
            entryPoint.commence(request, response, null);
        } catch (Exception e) {
            log.error("JWT 인증 필터 오류: {}", e.getMessage());
            request.setAttribute("authError", "AUTHENTICATION_FAILED");
            entryPoint.commence(request, response, null);
        }
    }

    /**
     * <p>HTTP 요청에서 JWT 토큰 추출</p>
     * <p>- Authorization 헤더에서 "Bearer " 접두사를 제거한 토큰 반환</p>
     *
     * @param req HTTP 요청
     * @return JWT 토큰 또는 null
     */
    private String resolveToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (!token.equalsIgnoreCase("undefined") && !token.isBlank()) {
                return token;
            }
        }
        return null;
    }
}