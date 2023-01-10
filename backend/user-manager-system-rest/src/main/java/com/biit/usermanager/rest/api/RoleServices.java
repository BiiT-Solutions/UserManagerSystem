package com.biit.usermanager.rest.api;

import com.biit.server.rest.BasicServices;
import com.biit.usermanager.core.controller.RoleController;
import com.biit.usermanager.core.controller.models.RoleDTO;
import com.biit.usermanager.core.converters.RoleConverter;
import com.biit.usermanager.core.converters.models.RoleConverterRequest;
import com.biit.usermanager.core.providers.RoleProvider;
import com.biit.usermanager.persistence.entities.Role;
import com.biit.usermanager.persistence.repositories.RoleRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleServices extends BasicServices<Role, RoleDTO, RoleRepository,
        RoleProvider, RoleConverterRequest, RoleConverter, RoleController> {

    public RoleServices(RoleController roleController) {
        super(roleController);
    }
}
