package com.example.funding.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class RequestLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, java.io.IOException {

        long start = System.currentTimeMillis();
        String method = req.getMethod();
        String uri = req.getRequestURI();
        String qs = req.getQueryString();
        String full = qs == null ? uri : uri + "?" + qs;

        try {
            chain.doFilter(req, res);
        } finally {
            long took = System.currentTimeMillis() - start;
            int status = res.getStatus();
            log.info("{} {} -> {} ({} ms)", method, full, status, took);
        }
    }
}
