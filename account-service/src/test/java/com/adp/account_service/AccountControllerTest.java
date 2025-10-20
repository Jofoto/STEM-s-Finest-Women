package com.adp.account_service;

import com.adp.account_service.client.Customer;
import com.adp.account_service.data_transfer.TokenRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private Customer client; // mock Data Service client

    @MockBean
    private TokenService tokenService; // mock JWT service

    @Test
    void testTokenEndpointSuccess() throws Exception {
        when(client.findByUsername("alice")).thenReturn(Map.of("username", "alice", "password", "secret"));
        when(tokenService.GenerateToken("alice")).thenReturn("fake-jwt-token");

        mvc.perform(post("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"alice\",\"password\":\"secret\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    void testTokenEndpointWrongPassword() throws Exception {
        when(client.findByUsername("alice"))
                .thenReturn(Map.of("username", "alice", "password", "secret"));

        mvc.perform(post("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"alice\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRegisterEndpoint() throws Exception {
        when(client.createCustomer(Mockito.any()))
                .thenReturn(Map.of("username", "bob", "email", "bob@example.com"));

        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"bob\",\"email\":\"bob@example.com\",\"password\":\"pw\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("bob"));
    }
}