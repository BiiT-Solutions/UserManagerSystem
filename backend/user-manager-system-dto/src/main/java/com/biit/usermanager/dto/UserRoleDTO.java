package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;
import com.biit.usermanager.entity.IRole;

public class UserRoleDTO extends ElementDTO {

    private OrganizationDTO organization;

    private ApplicationDTO application;

    private UserDTO user;

    private RoleDTO role;

    public UserRoleDTO() {
        super();
    }

    public UserRoleDTO(UserDTO user, RoleDTO role, OrganizationDTO organization, ApplicationDTO application) {
        this();
        setUser(user);
        setRole(role);
        setOrganization(organization);
        setApplication(application);
    }

    public OrganizationDTO getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationDTO organization) {
        this.organization = organization;
    }

    public ApplicationDTO getApplication() {
        return application;
    }

    public void setApplication(ApplicationDTO application) {
        this.application = application;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }
}
