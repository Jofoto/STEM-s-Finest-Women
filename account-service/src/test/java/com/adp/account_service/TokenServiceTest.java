package com.adp.account_service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TokenServiceTest {

    @Test
    void testGenerateAndValidateToken() {
        String secret = "shaimasSuperSecureJWTSecretKey123456";
        TokenService service = new TokenService(secret);

        String token = service.GenerateToken("alice");
        assertNotNull(token);

        String subject = service.validateToken(token);
        assertEquals("alice", subject);
    }
}