package com.pallas.communityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.pallas.communityservice", "com.pallas.integrations"})
@SuppressWarnings("PMD.UseUtilityClass")
public class CommunityServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(CommunityServiceApplication.class, args);
  }
}
