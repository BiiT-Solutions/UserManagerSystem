package com.biit.usermanager;


import com.biit.usermanager.rest.api.UserManagerSecurityService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

//@SpringBootApplication
//@ComponentScan({"com.biit.usermanager", "com.biit.server", "com.biit.server.security", "com.biit.messagebird.client"})
//@ConfigurationPropertiesScan({"com.biit.usermanager.rest", "com.biit.server.security.userguard"})
//@EntityScan({"com.biit.usermanager.persistence.entities", "com.biit.server.security.userguard"})
public class TestUserManagerServer {

    public static void main(String[] args) {
        SpringApplication.run(TestUserManagerServer.class, args);
    }

//    @Bean("securityService")
//    @Primary
//    public UserManagerSecurityService getSecurityService() {
//        return new UserManagerSecurityService();
//    }
}
