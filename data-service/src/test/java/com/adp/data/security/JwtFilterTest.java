package com.adp.data.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.Key;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JwtFilterTest {

    private JwtFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;
    private StringWriter stringWriter;

    private final String jwtSecret = "shaimasSuperSecureJWTSecretKey123456";
    private final String internalSecret = "dev-internal-secret";

    @BeforeEach
    void setup() throws Exception {
        filter = new JwtFilter();

        // Manually inject @Value fields
        var jwtField = JwtFilter.class.getDeclaredField("jwtSecret");
        jwtField.setAccessible(true);
        jwtField.set(filter, jwtSecret);

        var internalField = JwtFilter.class.getDeclaredField("internalSecret");
        internalField.setAccessible(true);
        internalField.set(filter, internalSecret);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);

        // Mock response writer
        stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testInternalBypassAllowed() throws IOException, ServletException {
        when(request.getHeader("X-Internal-Secret")).thenReturn(internalSecret);
        filter.doFilter(request, response, chain);
        verify(chain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void testMissingAuthHeader() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilter(request, response, chain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Missing or invalid Authorization header"));
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    void testValidJwtAllowsRequest() throws IOException, ServletException {
        // Generate valid JWT
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .setSubject("alice")
                .signWith(key)
                .compact();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void testInvalidJwtBlocksRequest() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token.value");

        filter.doFilter(request, response, chain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Invalid token"));
        verify(chain, never()).doFilter(request, response);
    }
}