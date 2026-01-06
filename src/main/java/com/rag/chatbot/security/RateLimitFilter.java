package com.rag.chatbot.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    @Value("${app.rate-limit.requests-per-minute}")
    private int requestsPerMinute;

    private static final long WINDOW_SIZE_MS = 60_000;

    private final Map<String, RequestCounter> counters = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientKey = resolveClientKey(request);
        long now = Instant.now().toEpochMilli();
        RequestCounter counter = counters.computeIfAbsent(clientKey, k -> new RequestCounter(now,0));

        synchronized (counter) {
            if (now - counter.windowStart >= WINDOW_SIZE_MS) {
                counter.windowStart = now;
                counter.count = 0;
            }

            if (counter.count >= requestsPerMinute) {
                response.sendError(
                        HttpStatus.TOO_MANY_REQUESTS.value(),
                        "Rate limit exceeded"
                );
                return;
            }

            counter.count++;
        }
        filterChain.doFilter(request, response);
    }
    private String resolveClientKey(HttpServletRequest request) {
        String apiKey= request.getHeader("X-API-KEY");
        return apiKey != null ? apiKey : request.getRemoteAddr();
    }

    private static class RequestCounter {
        long windowStart;
        int count;

        RequestCounter(long windowStart, int count) {
            this.windowStart = windowStart;
            this.count = count;
        }
    }
}
