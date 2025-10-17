package com.adp.account_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

    @GetMapping("/")
    public String running() {
        return "Account Service is running!";
    }

}
