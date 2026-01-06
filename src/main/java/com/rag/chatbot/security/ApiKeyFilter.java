package com.rag.chatbot.security;

import jakarta.persistence.Column;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class ApiKeyFilter  extends OncePerRequestFilter {

    @Value("${security.api-key}")
    private String expectedApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("Expected API key = {}", expectedApiKey);

        String apiKey = request.getHeader("X-API-KEY");

        if (apiKey==null || !apiKey.equals(expectedApiKey)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(),"Invalid API Key");
            return;
        }
        filterChain.doFilter(request,response);
    }
}
