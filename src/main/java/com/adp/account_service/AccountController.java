package com.adp.account_service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adp.account_service.client.Customer;
import com.adp.account_service.data_transfer.RegisterRequest;
import com.adp.account_service.data_transfer.TokenRequest;
import com.adp.account_service.data_transfer.TokenResponse;

@RestController
@RequestMapping("/")
public class AccountController {

    private final Customer client;

    
    private final TokenService tokenService;

    public AccountController(Customer client, TokenService tokenService) {
        this.client = client;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest newClient) {
        var created = client.createCustomer(newClient);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/token")
    public TokenResponse token(@RequestBody TokenRequest req) {
        String token = tokenService.GenerateToken(req.username());
        return new TokenResponse(token);
    }

}
