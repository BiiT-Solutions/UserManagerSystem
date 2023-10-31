package com.biit.usermanager.rest;


import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

@EnableCaching
@Configuration
@TestPropertySource("classpath:application.properties")
@ComponentScan({"com.biit.usermanager.rest"})
public class TestConfiguration {

}
