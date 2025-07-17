package com.biit.usermanager.core.utils;

import com.biit.usermanager.dto.ApplicationRoleDTO;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.UserGroupApplicationBackendServiceRole;

public final class RoleNameGenerator {

    private RoleNameGenerator() {

    }

    public static String createRoleName(BackendServiceRole backendServiceRole) {
        if (backendServiceRole == null) {
            return null;
        }
        return backendServiceRole.getId().getBackendService().getName().toUpperCase() + "_" + backendServiceRole.getId().getName().toUpperCase();
    }

    public static String createRoleName(BackendServiceRoleDTO backendServiceRole) {
        if (backendServiceRole == null) {
            return null;
        }
        return backendServiceRole.getId().getBackendService().getName().toUpperCase() + "_" + backendServiceRole.getId().getName().toUpperCase();
    }

    public static String createRoleName(ApplicationRoleDTO applicationRoleDTO) {
        if (applicationRoleDTO == null) {
            return null;
        }
        return applicationRoleDTO.getId().getApplication().getName().toUpperCase() + "_" + applicationRoleDTO.getId().getRole().getName().toUpperCase();
    }

    public static String createApplicationRoleName(UserApplicationBackendServiceRole userApplicationBackendServiceRole) {
        if (userApplicationBackendServiceRole == null) {
            return null;
        }
        return userApplicationBackendServiceRole.getId().getApplicationName().toUpperCase() + "_"
                + userApplicationBackendServiceRole.getId().getRoleName().replace(" ", "_").toUpperCase();
    }

    public static String createBackendRoleName(UserApplicationBackendServiceRole userApplicationBackendServiceRole) {
        if (userApplicationBackendServiceRole == null) {
            return null;
        }
        return userApplicationBackendServiceRole.getId().getBackendServiceName().toUpperCase() + "_"
                + userApplicationBackendServiceRole.getId().getBackendServiceRole().replace(" ", "_").toUpperCase();
    }

    public static String createApplicationRoleName(UserGroupApplicationBackendServiceRole userGroupApplicationBackendServiceRole) {
        if (userGroupApplicationBackendServiceRole == null) {
            return null;
        }
        return userGroupApplicationBackendServiceRole.getId().getApplicationName().toUpperCase() + "_"
                + userGroupApplicationBackendServiceRole.getId().getRoleName().toUpperCase();
    }

    public static String createBackendRoleName(UserGroupApplicationBackendServiceRole userGroupApplicationBackendServiceRole) {
        if (userGroupApplicationBackendServiceRole == null) {
            return null;
        }
        return userGroupApplicationBackendServiceRole.getId().getBackendServiceName().toUpperCase() + "_"
                + userGroupApplicationBackendServiceRole.getId().getBackendServiceRole().toUpperCase();
    }
}
