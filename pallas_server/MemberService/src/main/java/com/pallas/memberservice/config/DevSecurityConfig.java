package com.pallas.memberservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("dev")
public class DevSecurityConfig {

  @Bean
  @Order(1)
  public SecurityFilterChain devSwaggerSecurityFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
    return http.build();
  }
}
