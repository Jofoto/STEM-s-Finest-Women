package com.adp.account_service;

import org.springframework.web.bind.annotation.RestController;

import com.adp.account_service.client.Customer;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class ServiceController {

    @GetMapping("/")
    public String running() {
        return "Account Service is running!";
    }

}
