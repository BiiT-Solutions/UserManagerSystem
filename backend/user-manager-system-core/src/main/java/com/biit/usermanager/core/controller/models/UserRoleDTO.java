package com.biit.usermanager.core.controller.models;

import com.biit.server.controllers.models.ElementDTO;
import com.biit.usermanager.entity.IRole;

public class UserRoleDTO extends ElementDTO implements IRole<Long> {

    private OrganizationDTO organization;

    private UserDTO user;

    private RoleDTO role;

    public OrganizationDTO getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationDTO organization) {
        this.organization = organization;
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

    @Override
    public String getUniqueName() {
        return getOrganization().getUniqueName() + "_" + getRole().getName();
    }

    @Override
    public Long getUniqueId() {
        return getId();
    }
}
