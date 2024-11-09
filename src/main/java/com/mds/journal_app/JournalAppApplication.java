package com.mds.journal_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@ComponentScan(basePackages = "com.mds")
@EnableMongoAuditing
public class JournalAppApplication {
  public static void main(String[] args) {
    SpringApplication.run(JournalAppApplication.class, args);
  }
}
