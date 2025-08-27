package com.example.funding.filter;

import com.example.funding.common.CustomUserPrincipal;
import com.example.funding.dao.UserDao;
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
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtAuthenticationEntryPoint entryPoint;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final JwtProvider jwtProvider;

    private final UserDao userDao;

    private static final String[] WHITELIST = {
            "/api/v1/auth/signIn", "/api/v1/auth/signUp",        // 로그인/회원가입
            "/public/**", "/swagger-ui/**", "/v3/api-docs/**",
            "/error", "/favicon.ico"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        if ("OPTIONS".equalsIgnoreCase(req.getMethod()))
            return true;
        String path = req.getRequestURI();
        for (String p : WHITELIST)
            if (pathMatcher.match(p, path))
                return true;
        return false;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        if (token == null) {
            request.setAttribute("authError", "TOKEN_MISSING");
            entryPoint.commence(request, response, null);
            return;
        }

        try {
            Claims claims = jwtProvider.validateAndGetClaims(token);
            String email = claims.get("email", String.class);
            if (email == null || userDao.findByEmail(email) == null) {
                request.setAttribute("authError", "TOKEN_INVALID");
                entryPoint.commence(request, response, null);
                return;
            }
            String role = claims.get("role", String.class);
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());
            Long userId = ((Number) claims.get("userId")).longValue();

            CustomUserPrincipal principal = new CustomUserPrincipal(userId, email, List.of(authority));
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

    private String resolveToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer "))
            return header.substring(7);
        return null;
    }
}