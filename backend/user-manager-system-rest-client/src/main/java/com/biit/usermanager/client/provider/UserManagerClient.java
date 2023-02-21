package com.biit.usermanager.client.provider;

import com.biit.server.client.SecurityClient;
import org.springframework.stereotype.Service;

@Service
public class UserManagerClient {

    private final UserUrlConstructor userUrlConstructor;

    private final SecurityClient securityClient;

    public UserManagerClient(UserUrlConstructor userUrlConstructor, SecurityClient securityClient) {
        this.userUrlConstructor = userUrlConstructor;
        this.securityClient = securityClient;
    }


}
