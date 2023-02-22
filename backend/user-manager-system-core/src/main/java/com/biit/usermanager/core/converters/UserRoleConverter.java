package com.biit.usermanager.core.converters;


import com.biit.server.controller.converters.ElementConverter;
import com.biit.usermanager.core.converters.models.*;
import com.biit.usermanager.dto.UserRoleDTO;
import com.biit.usermanager.persistence.entities.UserRole;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserRoleConverter extends ElementConverter<UserRole, UserRoleDTO, UserRoleConverterRequest> {
    private final RoleConverter roleConverter;
    private final UserConverter userConverter;
    private final OrganizationConverter organizationConverter;

    private final ApplicationConverter applicationConverter;

    public UserRoleConverter(RoleConverter roleConverter, UserConverter userConverter,
                             OrganizationConverter organizationConverter, ApplicationConverter applicationConverter) {
        this.roleConverter = roleConverter;
        this.userConverter = userConverter;
        this.organizationConverter = organizationConverter;
        this.applicationConverter = applicationConverter;
    }


    @Override
    protected UserRoleDTO convertElement(UserRoleConverterRequest from) {
        final UserRoleDTO userRoleDTO = new UserRoleDTO();
        BeanUtils.copyProperties(from.getEntity(), userRoleDTO);
        userRoleDTO.setRole(roleConverter.convert(new RoleConverterRequest(from.getEntity().getRole())));
        userRoleDTO.setOrganization(organizationConverter.convert(new OrganizationConverterRequest(from.getEntity().getOrganization())));
        userRoleDTO.setUser(userConverter.convert(new UserConverterRequest(from.getEntity().getUser())));
        userRoleDTO.setApplication(applicationConverter.convert(new ApplicationConverterRequest(from.getEntity().getApplication())));
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
        userRole.setApplication(applicationConverter.reverse(to.getApplication()));
        return userRole;
    }
}
