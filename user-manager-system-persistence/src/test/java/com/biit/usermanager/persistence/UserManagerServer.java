package com.biit.usermanager.persistence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@SpringBootApplication
@Service
public class UserManagerServer {

    public static void main(String[] args) {
        SpringApplication.run(UserManagerServer.class, args);
    }
}
