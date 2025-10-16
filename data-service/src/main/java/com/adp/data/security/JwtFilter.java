//This filter intercepts every req to the data service API and checks for valid JWT in Auth header
package com.adp.data.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtFilter implements Filter {

    @Value("${app.jwt.secret}") private String jwtSecret; //must match same in acc service
    @Value("${app.internal.secret}") private String internalSecret;

    private boolean isInternal(HttpServletRequest req) {
        String header = req.getHeader("X-Internal-Secret");
        return header != null && header.equals(internalSecret);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();
        // Allow root health check
        if ("/api/".equals(req.getContextPath() + "/") || "/api".equals(path)) {
            chain.doFilter(request, response); return;
        }

        // Internal bypass for Account service (registration)
        if (isInternal(req)) { chain.doFilter(request, response); return; }

        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Missing or invalid Authorization header");
            return;
        }
        String token = auth.substring(7);
        try {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            chain.doFilter(request, response);
        } catch (JwtException e) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Invalid token");
        }
    }
}