package com.biit.usermanager.core.controller;


import com.biit.usermanager.core.controller.models.UserRoleDTO;
import com.biit.usermanager.core.converters.UserRoleConverter;
import com.biit.usermanager.core.converters.models.UserRoleConverterRequest;
import com.biit.usermanager.core.providers.UserRoleProvider;
import com.biit.usermanager.persistence.entities.UserRole;
import com.biit.usermanager.persistence.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserRoleController extends BasicInsertableController<UserRole, UserRoleDTO, UserRoleRepository,
        UserRoleProvider, UserRoleConverterRequest, UserRoleConverter> {

    @Autowired
    protected UserRoleController(UserRoleProvider provider, UserRoleConverter converter) {
        super(provider, converter);
    }

    @Override
    protected UserRoleConverterRequest createConverterRequest(UserRole entity) {
        return new UserRoleConverterRequest(entity);
    }
}
