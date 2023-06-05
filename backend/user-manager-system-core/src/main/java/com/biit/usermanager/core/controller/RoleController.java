package com.biit.usermanager.core.controller;


import com.biit.server.controller.BasicInsertableController;
import com.biit.usermanager.core.converters.RoleConverter;
import com.biit.usermanager.core.converters.models.RoleConverterRequest;
import com.biit.usermanager.core.exceptions.RoleNotFoundException;
import com.biit.usermanager.core.providers.RoleProvider;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.persistence.entities.Role;
import com.biit.usermanager.persistence.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class RoleController extends BasicInsertableController<Role, RoleDTO, RoleRepository,
        RoleProvider, RoleConverterRequest, RoleConverter> {

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    protected RoleController(RoleProvider provider, RoleConverter converter) {
        super(provider, converter);
    }

    @Override
    protected RoleConverterRequest createConverterRequest(Role entity) {
        return new RoleConverterRequest(entity);
    }

    public RoleDTO getByName(String name) {
        return getConverter().convert(new RoleConverterRequest(getProvider().findByName(name).orElseThrow(() -> new RoleNotFoundException(this.getClass(),
                "No Role with name '" + name + "' found on the system."))));
    }
}
