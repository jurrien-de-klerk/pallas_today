package com.pallas.storyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.pallas.storyservice", "com.pallas.integrations"})
@SuppressWarnings("PMD.UseUtilityClass")
public class StoryServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(StoryServiceApplication.class, args);
  }
}
