package com.biit.usermanager.rest.api;


import com.biit.server.rest.SecurityService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service("securityService")
public class UserManagerSecurityService extends SecurityService {

    @Override
    public String getViewerPrivilege() {
        return "USERMANAGERSYSTEM_VIEWER";
    }

    @Override
    public String getAdminPrivilege() {
        return "USERMANAGERSYSTEM_ADMIN";
    }

    @Override
    public String getEditorPrivilege() {
        return "USERMANAGERSYSTEM_EDITOR";
    }

    @Override
    public String getTokenPrivilege() {
        return "USERMANAGERSYSTEM_TOKEN";
    }

    @Override
    public String getOrganizationAdminPrivilege() {
        return "USERMANAGERSYSTEM_ORGANIZATION_ADMIN";
    }
}
