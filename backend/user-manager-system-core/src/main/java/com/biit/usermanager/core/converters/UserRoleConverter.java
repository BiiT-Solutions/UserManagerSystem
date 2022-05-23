package com.biit.usermanager.core.converters;

import com.biit.usermanager.core.controller.models.UserRoleDTO;
import com.biit.usermanager.core.converters.models.OrganizationConverterRequest;
import com.biit.usermanager.core.converters.models.RoleConverterRequest;
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.core.converters.models.UserRoleConverterRequest;
import com.biit.usermanager.persistence.UserRole;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserRoleConverter extends ElementConverter<UserRole, UserRoleDTO, UserRoleConverterRequest> {
    private final RoleConverter roleConverter;
    private final UserConverter userConverter;
    private final OrganizationConverter organizationConverter;

    public UserRoleConverter(RoleConverter roleConverter, UserConverter userConverter, OrganizationConverter organizationConverter) {
        this.roleConverter = roleConverter;
        this.userConverter = userConverter;
        this.organizationConverter = organizationConverter;
    }


    @Override
    public UserRoleDTO convert(UserRoleConverterRequest from) {
        final UserRoleDTO userRoleDTO = new UserRoleDTO();
        BeanUtils.copyProperties(from.getEntity(), userRoleDTO);
        userRoleDTO.setRole(roleConverter.convert(new RoleConverterRequest(from.getEntity().getRole())));
        userRoleDTO.setOrganization(organizationConverter.convert(new OrganizationConverterRequest(from.getEntity().getOrganization())));
        userRoleDTO.setUser(userConverter.convert(new UserConverterRequest(from.getEntity().getUser())));
        return userRoleDTO;
    }

    @Override
    public UserRole reverse(UserRoleDTO to) {
        if (to == null) {
            return null;
        }
        final UserRole userRole = new UserRole();
        BeanUtils.copyProperties(to, userRole);
        userRole.setRole(roleConverter.reverse(to.getRole()));
        userRole.setOrganization(organizationConverter.reverse(to.getOrganization()));
        userRole.setUser(userConverter.reverse(to.getUser()));
        return userRole;
    }
}
