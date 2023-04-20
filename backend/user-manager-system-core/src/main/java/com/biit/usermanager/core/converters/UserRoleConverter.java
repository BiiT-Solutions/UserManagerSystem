package com.biit.usermanager.core.converters;


import com.biit.server.controller.converters.ElementConverter;
import com.biit.usermanager.core.converters.models.*;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.core.providers.GroupProvider;
import com.biit.usermanager.dto.UserRoleDTO;
import com.biit.usermanager.persistence.entities.UserRole;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserRoleConverter extends ElementConverter<UserRole, UserRoleDTO, UserRoleConverterRequest> {
    private final RoleConverter roleConverter;
    private final UserConverter userConverter;
    private final GroupConverter groupConverter;
    private final GroupProvider groupProvider;
    private final ApplicationConverter applicationConverter;

    private final ApplicationProvider applicationProvider;


    public UserRoleConverter(RoleConverter roleConverter, UserConverter userConverter,
                             GroupConverter groupConverter, GroupProvider groupProvider,
                             ApplicationConverter applicationConverter, ApplicationProvider applicationProvider) {
        this.roleConverter = roleConverter;
        this.userConverter = userConverter;
        this.groupConverter = groupConverter;
        this.groupProvider = groupProvider;
        this.applicationConverter = applicationConverter;
        this.applicationProvider = applicationProvider;
    }


    @Override
    protected UserRoleDTO convertElement(UserRoleConverterRequest from) {
        final UserRoleDTO userRoleDTO = new UserRoleDTO();
        BeanUtils.copyProperties(from.getEntity(), userRoleDTO);
        userRoleDTO.setRole(roleConverter.convert(new RoleConverterRequest(from.getEntity().getRole())));
        if (from.getEntity().getGroup() != null) {
            userRoleDTO.setGroup(groupConverter.convert(
                    new GroupConverterRequest(groupProvider.get(from.getEntity().getGroup().getId()))));
        }
        userRoleDTO.setUser(userConverter.convert(new UserConverterRequest(from.getEntity().getUser())));
        if (from.getEntity().getApplication() != null) {
            userRoleDTO.setApplication(applicationConverter.convert(
                    new ApplicationConverterRequest(applicationProvider.get(from.getEntity().getApplication().getId()))));
        }
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
        userRole.setGroup(groupConverter.reverse(to.getGroup()));
        userRole.setUser(userConverter.reverse(to.getUser()));
        userRole.setApplication(applicationConverter.reverse(to.getApplication()));
        return userRole;
    }
}
