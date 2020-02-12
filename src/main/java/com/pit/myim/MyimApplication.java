package com.pit.myim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MyimApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyimApplication.class, args);
    }


    @Bean
    public static RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
