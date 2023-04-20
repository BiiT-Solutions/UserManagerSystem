package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;

public class UserRoleDTO extends ElementDTO {

    private GroupDTO group;

    private ApplicationDTO application;

    private UserDTO user;

    private RoleDTO role;

    public UserRoleDTO() {
        super();
    }

    public UserRoleDTO(UserDTO user, RoleDTO role, GroupDTO group, ApplicationDTO application) {
        this();
        setUser(user);
        setRole(role);
        setGroup(group);
        setApplication(application);
    }

    public GroupDTO getGroup() {
        return group;
    }

    public void setGroup(GroupDTO group) {
        this.group = group;
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
