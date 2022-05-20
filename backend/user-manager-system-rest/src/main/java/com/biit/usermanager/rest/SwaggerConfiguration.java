package com.biit.usermanager.rest;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfiguration {
    private static final String SWAGGER_GROUP = "user-manager-system";
    private static final String SWAGGER_TITLE = "User Manager System";
    private static final String SWAGGER_DESCRIPTION = "User Manager System";
    private static final String SWAGGER_README = SWAGGER_TITLE + " Documentation";
    private static final String SWAGGER_URL = "https://biit-solutions.com/";
    private static final String SWAGGER_DEFAULT_VERSION = "Dev";
    private static final String[] PACKAGES_TO_SCAN = new String[]{"com.biit.usermanager"};

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group(SWAGGER_GROUP)
                .packagesToScan(PACKAGES_TO_SCAN)
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI userManagerSystemOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(SWAGGER_TITLE)
                        .description(SWAGGER_DESCRIPTION)
                        .version(SwaggerConfiguration.class.getPackage().getImplementationVersion() != null ?
                                SwaggerConfiguration.class.getPackage().getImplementationVersion() : SWAGGER_DEFAULT_VERSION))
                .externalDocs(new ExternalDocumentation()
                        .description(SWAGGER_README)
                        .url(SWAGGER_URL));
    }
}
