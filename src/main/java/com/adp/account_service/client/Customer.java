package com.adp.account_service.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.adp.account_service.data_transfer.RegisterRequest;

@Service
public class Customer {
    private final RestTemplate rest;
    private final String url;

    public Customer(RestTemplate restTemplate, @Value("${data.service.url}") String dataServiceUrl) {
        this.rest = restTemplate;
        this.url = dataServiceUrl;
    }

    public Map createCustomer(RegisterRequest request) { //sends post request to data service to create a new customer
        return rest.postForObject(url + "/customers", request, Map.class);
    }


    
}
