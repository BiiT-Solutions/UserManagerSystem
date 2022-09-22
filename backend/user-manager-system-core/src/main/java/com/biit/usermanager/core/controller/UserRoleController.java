package com.biit.usermanager.core.controller;


import com.biit.server.controller.BasicInsertableController;
import com.biit.usermanager.core.controller.models.OrganizationDTO;
import com.biit.usermanager.core.controller.models.UserDTO;
import com.biit.usermanager.core.controller.models.UserRoleDTO;
import com.biit.usermanager.core.converters.OrganizationConverter;
import com.biit.usermanager.core.converters.UserConverter;
import com.biit.usermanager.core.converters.UserRoleConverter;
import com.biit.usermanager.core.converters.models.UserRoleConverterRequest;
import com.biit.usermanager.core.providers.UserRoleProvider;
import com.biit.usermanager.persistence.entities.UserRole;
import com.biit.usermanager.persistence.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserRoleController extends BasicInsertableController<UserRole, UserRoleDTO, UserRoleRepository,
        UserRoleProvider, UserRoleConverterRequest, UserRoleConverter> {
    private final UserConverter userConverter;
    private final OrganizationConverter organizationConverter;

    @Autowired
    protected UserRoleController(UserRoleProvider provider, UserRoleConverter converter, UserConverter userConverter,
                                 OrganizationConverter organizationConverter) {
        super(provider, converter);
        this.userConverter = userConverter;
        this.organizationConverter = organizationConverter;
    }

    @Override
    protected UserRoleConverterRequest createConverterRequest(UserRole entity) {
        return new UserRoleConverterRequest(entity);
    }

    public List<UserRoleDTO> getByUserAndOrganization(UserDTO userDTO, OrganizationDTO organizationDTO) {
        return converter.convertAll(provider.findByUserAndOrganization(userConverter.reverse(userDTO),
                organizationConverter.reverse(organizationDTO)).stream()
                .map(this::createConverterRequest).collect(Collectors.toList()));
    }
}
