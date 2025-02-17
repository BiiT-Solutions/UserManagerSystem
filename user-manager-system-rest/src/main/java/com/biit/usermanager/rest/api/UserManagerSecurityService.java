package com.biit.usermanager.rest.api;


import com.biit.server.rest.SecurityService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service("securityService")
public class UserManagerSecurityService extends SecurityService {

    public String getViewerPrivilege() {
        return "USERMANAGERSYSTEM_VIEWER";
    }

    public String getAdminPrivilege() {
        return "USERMANAGERSYSTEM_ADMIN";
    }

    public String getEditorPrivilege() {
        return "USERMANAGERSYSTEM_EDITOR";
    }

    public String getTokenPrivilege() {
        return "USERMANAGERSYSTEM_TOKEN";
    }
}
