package com.healthapp.itemhealth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ItemhealthApplication {

  public static void main(String[] args) {
    SpringApplication.run(ItemhealthApplication.class, args);
  }
}
