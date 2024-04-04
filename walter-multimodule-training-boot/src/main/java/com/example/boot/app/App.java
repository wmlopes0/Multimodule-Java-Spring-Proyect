package com.example.boot.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.example.boot",
    "com.example.contract",
    "com.example.application",
    "com.example.domain",
    "com.example.infrastructure.repository"})
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
