package com.biit.server.security;

import com.biit.usermanager.core.controller.UserController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = {"com.biit.usermanager", "com.biit.server", "com.biit.messagebird.client"})
@ConfigurationPropertiesScan({"com.biit.server.security.userguard"})
public class UserManagerServer {

    public static void main(String[] args) {
        SpringApplication.run(UserManagerServer.class, args);
    }

}
