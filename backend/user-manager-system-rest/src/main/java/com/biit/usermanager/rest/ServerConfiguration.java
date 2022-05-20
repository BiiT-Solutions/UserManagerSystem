package com.biit.usermanager.rest;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.*;

@Configuration
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource(value = "file:${EXTERNAL_CONFIG_FILE}", ignoreResourceNotFound = true)
})
@ComponentScan({"com.biit.usermanager"})
public class ServerConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
