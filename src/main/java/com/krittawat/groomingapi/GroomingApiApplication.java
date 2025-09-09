package com.krittawat.groomingapi;

import com.krittawat.groomingapi.config.PromptPayProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(PromptPayProperties.class)
public class GroomingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroomingApiApplication.class, args);
    }

}
