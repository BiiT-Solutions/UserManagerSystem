package com.biit.usermanager.rest.api;


import com.biit.server.rest.SecurityService;
import com.biit.server.security.IUserOrganizationProvider;
import com.biit.server.security.model.IUserOrganization;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service("securityService")
public class UserManagerSecurityService extends SecurityService {


    public UserManagerSecurityService(List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProviders) {
        super(userOrganizationProviders);
    }

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
