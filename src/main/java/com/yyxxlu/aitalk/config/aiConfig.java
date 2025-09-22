package com.yyxxlu.aitalk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class aiConfig {
    @Value("${ai.key}")
    private String apiKey;
    @Value("${ai.base-url}")
    private String baseUrl;
}
