package com.example.flame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class FlameApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlameApplication.class, args);
    }

    // Menambahkan Bean RestTemplate agar bisa kita @Autowired nanti
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}