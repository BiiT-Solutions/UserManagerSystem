package com.biit.usermanager.client.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserUrlConstructor {

    @Value("${usermanager.server.url}")
    private String userManagerServerUrl;

    public String getUserManagerServerUrl() {
        return userManagerServerUrl;
    }

}
