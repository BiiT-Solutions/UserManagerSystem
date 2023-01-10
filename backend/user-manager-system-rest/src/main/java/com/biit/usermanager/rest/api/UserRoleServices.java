package com.biit.usermanager.rest.api;

import com.biit.server.rest.BasicServices;
import com.biit.usermanager.core.controller.UserRoleController;
import com.biit.usermanager.core.controller.models.UserRoleDTO;
import com.biit.usermanager.core.converters.UserRoleConverter;
import com.biit.usermanager.core.converters.models.UserRoleConverterRequest;
import com.biit.usermanager.core.providers.UserRoleProvider;
import com.biit.usermanager.persistence.entities.UserRole;
import com.biit.usermanager.persistence.repositories.UserRoleRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles/users")
public class UserRoleServices extends BasicServices<UserRole, UserRoleDTO, UserRoleRepository,
        UserRoleProvider, UserRoleConverterRequest, UserRoleConverter, UserRoleController> {

    public UserRoleServices(UserRoleController userRoleController) {
        super(userRoleController);
    }
}
