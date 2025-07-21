package com.biznify.partner.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
@Configuration
public class WebClientConfig {

    @Bean
   @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }



}
