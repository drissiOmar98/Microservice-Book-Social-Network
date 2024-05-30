package com.example.bookserver.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//Ensure that the Feign client configuration includes the newly created interceptor.
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignClientInterceptor();
    }
}
