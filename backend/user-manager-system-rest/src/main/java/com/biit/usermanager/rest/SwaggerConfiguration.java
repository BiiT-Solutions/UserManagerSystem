package com.biit.usermanager.rest;

import com.biit.server.rest.DefaultSwaggerConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration extends DefaultSwaggerConfiguration {
    private static final String SWAGGER_GROUP = "user-manager-system";
    private static final String SWAGGER_TITLE = "User Manager System";
    private static final String SWAGGER_DESCRIPTION = "User Manager System";
    private static final String[] PACKAGES_TO_SCAN = new String[]{"com.biit.usermanager", "com.biit.server.rest", "com.biit.server.security.rest"};

    @Override
    public String getSwaggerTitle() {
        return SWAGGER_TITLE;
    }

    @Override
    public String getSwaggerDescription() {
        return SWAGGER_TITLE;
    }

    @Override
    public String getSwaggerGroup() {
        return SWAGGER_GROUP;
    }

    @Override
    public String[] getPackagesToScan() {
        return PACKAGES_TO_SCAN;
    }
}
