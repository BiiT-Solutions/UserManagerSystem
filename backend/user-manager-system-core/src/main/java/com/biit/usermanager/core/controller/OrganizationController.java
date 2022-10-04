package com.biit.usermanager.core.controller;


import com.biit.server.controller.BasicInsertableController;
import com.biit.usermanager.core.controller.models.OrganizationDTO;
import com.biit.usermanager.core.converters.OrganizationConverter;
import com.biit.usermanager.core.converters.models.OrganizationConverterRequest;
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.providers.OrganizationProvider;
import com.biit.usermanager.persistence.entities.Organization;
import com.biit.usermanager.persistence.repositories.OrganizationRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class OrganizationController extends BasicInsertableController<Organization, OrganizationDTO, OrganizationRepository,
        OrganizationProvider, OrganizationConverterRequest, OrganizationConverter> {

    @Autowired
    protected OrganizationController(OrganizationProvider provider, OrganizationConverter converter) {
        super(provider, converter);
    }

    @Override
    protected OrganizationConverterRequest createConverterRequest(Organization entity) {
        return new OrganizationConverterRequest(entity);
    }

    public OrganizationDTO getByName(String name) {
        return converter.convert(new OrganizationConverterRequest(provider.findByName(name).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                "No User with username '" + name + "' found on the system."))));}

}
