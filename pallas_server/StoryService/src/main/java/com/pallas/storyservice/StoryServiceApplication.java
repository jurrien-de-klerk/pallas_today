package com.pallas.storyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StoryServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(StoryServiceApplication.class, args);
  }

  @GetMapping("/hello")
  public String hello() {
    return "world!";
  }
}
