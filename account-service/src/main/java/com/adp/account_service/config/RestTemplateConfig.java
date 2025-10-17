package com.adp.account_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.ClientHttpRequestInterceptor;

import java.util.List;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(@Value("${app.internal.secret}") String internalSecret) {
        RestTemplate rt = new RestTemplate(); //reusable object to make requests between services
        ClientHttpRequestInterceptor internalAuth = (req, body, exec) -> {
        	req.getHeaders().add("X-Internal-Secret", internalSecret);
        	return exec.execute(req, body);
        };
        rt.setInterceptors(List.of(internalAuth));
        return rt;
    }
}
