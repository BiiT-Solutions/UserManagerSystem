package com.biit.usermanager.dto.utils;

import com.biit.usermanager.dto.ApplicationRoleDTO;
import com.biit.usermanager.dto.BackendServiceRoleDTO;

public final class RoleNameGenerator {

    private RoleNameGenerator() {

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
}
