package com.biit.usermanager.core.utils;

import com.biit.usermanager.dto.ApplicationRoleDTO;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;

public final class RoleNameGenerator {

    private RoleNameGenerator() {

    }

    public static String createRoleName(BackendServiceRole backendServiceRole) {
        if (backendServiceRole == null) {
            return null;
        }
        return backendServiceRole.getId().getService().getName().toUpperCase() + "_" + backendServiceRole.getId().getName().toUpperCase();
    }

    public static String createRoleName(BackendServiceRoleDTO backendServiceRole) {
        if (backendServiceRole == null) {
            return null;
        }
        return backendServiceRole.getId().getService().getName().toUpperCase() + "_" + backendServiceRole.getId().getName().toUpperCase();
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
                + userApplicationBackendServiceRole.getId().getRoleName().toUpperCase();
    }

    public static String createBackendRoleName(UserApplicationBackendServiceRole userApplicationBackendServiceRole) {
        if (userApplicationBackendServiceRole == null) {
            return null;
        }
        return userApplicationBackendServiceRole.getId().getBackendServiceName().toUpperCase() + "_"
                + userApplicationBackendServiceRole.getId().getBackendServiceRole().toUpperCase();
    }
}
