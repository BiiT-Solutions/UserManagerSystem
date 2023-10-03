package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;

public class UserRoleDTO extends ElementDTO {

    private GroupDTO group;

    private UserDTO user;

    private RoleDTO role;

    public UserRoleDTO() {
        super();
    }

    public UserRoleDTO(UserDTO user, RoleDTO role, GroupDTO group) {
        this();
        setUser(user);
        setRole(role);
        setGroup(group);
    }

    public GroupDTO getGroup() {
        return group;
    }

    public void setGroup(GroupDTO group) {
        this.group = group;
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
    public String toString() {
        return "UserRoleDTO{"
                + "group=" + group
                + ", user=" + user
                + ", role=" + role
                + "}";
    }
}
