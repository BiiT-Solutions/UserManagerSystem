package com.biit.usermanager.rest;


import com.biit.usermanager.logger.UserManagerLogger;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.servlet.DispatcherServlet;

//Avoid Swagger redirecting https to http
@OpenAPIDefinition(servers = {@Server(url = "${server.servlet.context-path}", description = "Default Server URL")})
@SpringBootApplication
@ComponentScan({"com.biit.usermanager", "com.biit.server", "com.biit.server.security", "com.biit.messagebird.client", "com.biit.kafka"})
@ConfigurationPropertiesScan({"com.biit.usermanager.rest"})
//@EnableJpaRepositories({"com.biit.usermanager.persistence.repositories"})
@EntityScan({"com.biit.usermanager.persistence.entities", "com.biit.server.security.userguard"})
public class UserManagerServer {

    public static void main(String[] args) {
        SpringApplication.run(UserManagerServer.class, args);
    }


    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        return new LoggableDispatcherServlet();
    }


    @Bean
    public ApplicationListener<ContextRefreshedEvent> startupLoggingListener() {
        return event -> UserManagerLogger.info(UserManagerServer.class, "### Server started ###");
    }

    //For allowing properties as list in @Values
    @Bean
    public ConversionService conversionService() {
        return new DefaultConversionService();
    }

}
