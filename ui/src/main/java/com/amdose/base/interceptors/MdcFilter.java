package com.amdose.base.interceptors;

import com.amdose.base.constants.AppConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Alaa Jawhar
 */
@Component
public class MdcFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        MDC.put(AppConstants.TRANSACTION_ID, UUID.randomUUID().toString());
        filterChain.doFilter(servletRequest, servletResponse);
        MDC.clear();
    }
}
