package com.biit.usermanager.core.controller;


import com.biit.server.controller.BasicInsertableController;
import com.biit.usermanager.core.converters.RoleConverter;
import com.biit.usermanager.core.converters.models.RoleConverterRequest;
import com.biit.usermanager.core.providers.RoleProvider;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.persistence.entities.Role;
import com.biit.usermanager.persistence.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RoleController extends BasicInsertableController<Role, RoleDTO, RoleRepository,
        RoleProvider, RoleConverterRequest, RoleConverter> {

    @Autowired
    protected RoleController(RoleProvider provider, RoleConverter converter) {
        super(provider, converter);
    }

    @Override
    protected RoleConverterRequest createConverterRequest(Role entity) {
        return new RoleConverterRequest(entity);
    }
}
