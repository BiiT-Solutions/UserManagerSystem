package com.biit.usermanager.rest.api;


import com.biit.server.controllers.models.CreatedElementDTO;
import com.biit.server.persistence.entities.CreatedElement;
import com.biit.server.rest.SecurityService;
import com.biit.server.security.IUserOrganizationProvider;
import com.biit.server.security.model.IUserOrganization;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Primary
@Service("securityService")
public class UserManagerSecurityService extends SecurityService {

    private final List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProviders;

    public UserManagerSecurityService(List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProviders) {
        this.userOrganizationProviders = userOrganizationProviders;
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

    public void canBeDoneByOrganizationAdmin(CreatedElementDTO dto, Authentication authentication) {
        if (!userOrganizationProviders.isEmpty()) {
            super.canBeDoneByOrganizationAdmin(dto, authentication, userOrganizationProviders.get(0));
        }
    }


    public void canBeDoneByOrganizationAdmin(CreatedElement entity, Authentication authentication) {
        if (!userOrganizationProviders.isEmpty()) {
            super.canBeDoneByOrganizationAdmin(entity, authentication, userOrganizationProviders.get(0));
        }
    }

    public void canBeDoneByDifferentUsers(Collection<String> userNames, Authentication authentication) {
        if (!userOrganizationProviders.isEmpty()) {
            super.canBeDoneByDifferentUsers(userNames, authentication, userOrganizationProviders.get(0));
        }
    }

    public void canBeDoneByDifferentUsers(String userName, Authentication authentication) {
        if (!userOrganizationProviders.isEmpty()) {
            super.canBeDoneByDifferentUsers(userName, authentication, userOrganizationProviders.get(0));
        }
    }

    public void checkHasOrganizationAdminAccess(String organizationName, Authentication authentication,
                                                String... otherAuthoritiesAllowed) {
        if (!userOrganizationProviders.isEmpty()) {
            super.checkHasOrganizationAdminAccess(organizationName, authentication, userOrganizationProviders.get(0), otherAuthoritiesAllowed);
        }
    }

    public IUserOrganization getRequiredUserOrganization(Authentication authentication,
                                                         String... otherAuthoritiesAllowed) {
        if (!userOrganizationProviders.isEmpty()) {
            return super.getRequiredUserOrganization(authentication, userOrganizationProviders.get(0), otherAuthoritiesAllowed);
        }
        return null;
    }

    public void checkCreatedOn(CreatedElementDTO elementDTO, Authentication authentication,
                               String... otherAuthoritiesAllowed) {
        if (!userOrganizationProviders.isEmpty()) {
            super.checkCreatedOn(elementDTO, authentication, userOrganizationProviders.get(0), otherAuthoritiesAllowed);
        }
    }
}
