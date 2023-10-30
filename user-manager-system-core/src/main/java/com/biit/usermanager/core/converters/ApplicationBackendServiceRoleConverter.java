package com.biit.usermanager.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.server.converters.ConverterUtils;
import com.biit.usermanager.core.converters.models.ApplicationBackendServiceRoleConverterRequest;
import com.biit.usermanager.core.converters.models.ApplicationRoleConverterRequest;
import com.biit.usermanager.core.converters.models.BackendServiceRoleConverterRequest;
import com.biit.usermanager.core.providers.ApplicationRoleProvider;
import com.biit.usermanager.core.providers.BackendServiceRoleProvider;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleDTO;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleIdDTO;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRoleId;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ApplicationBackendServiceRoleConverter extends ElementConverter<ApplicationBackendServiceRole,
        ApplicationBackendServiceRoleDTO, ApplicationBackendServiceRoleConverterRequest> {

    private final ApplicationRoleConverter applicationRoleConverter;
    private final BackendServiceRoleConverter backendServiceRoleConverter;

    private final ApplicationRoleProvider applicationRoleProvider;

    private final BackendServiceRoleProvider backendServiceRoleProvider;

    public ApplicationBackendServiceRoleConverter(ApplicationRoleConverter applicationRoleConverter, BackendServiceRoleConverter backendServiceRoleConverter,
                                                  ApplicationRoleProvider applicationRoleProvider, BackendServiceRoleProvider backendServiceRoleProvider) {
        this.applicationRoleConverter = applicationRoleConverter;
        this.backendServiceRoleConverter = backendServiceRoleConverter;
        this.applicationRoleProvider = applicationRoleProvider;
        this.backendServiceRoleProvider = backendServiceRoleProvider;
    }

    @Override
    protected ApplicationBackendServiceRoleDTO convertElement(ApplicationBackendServiceRoleConverterRequest from) {
        final ApplicationBackendServiceRoleDTO applicationBackendServiceRoleDTO = new ApplicationBackendServiceRoleDTO();
        BeanUtils.copyProperties(from.getEntity(), applicationBackendServiceRoleDTO, ConverterUtils.getNullPropertyNames(from.getEntity()));

        ApplicationRole applicationRole = from.getEntity().getId().getApplicationRole();
        BackendServiceRole backendServiceRole = from.getEntity().getId().getBackendServiceRole();

        if (!Hibernate.isInitialized(applicationRole)) {
            applicationRole = applicationRoleProvider.findByApplicationIdAndRoleId(
                    from.getEntity().getId().getApplicationRole().getId().getApplication().getName(),
                    from.getEntity().getId().getApplicationRole().getId().getRole().getName()
            ).orElse(null);
        }

        if (!Hibernate.isInitialized(backendServiceRole)) {
            backendServiceRole = backendServiceRoleProvider.findByBackendServiceAndName(
                    from.getEntity().getId().getBackendServiceRole().getId().getBackendService().getName(),
                    from.getEntity().getId().getBackendServiceRole().getId().getName()
            ).orElse(null);
        }

        applicationBackendServiceRoleDTO.setId(new ApplicationBackendServiceRoleIdDTO(
                applicationRoleConverter.convertElement(new ApplicationRoleConverterRequest(applicationRole)),
                backendServiceRoleConverter.convertElement(new BackendServiceRoleConverterRequest(backendServiceRole)))
        );
        return applicationBackendServiceRoleDTO;
    }

    @Override
    public ApplicationBackendServiceRole reverse(ApplicationBackendServiceRoleDTO to) {
        if (to == null) {
            return null;
        }
        final ApplicationBackendServiceRole applicationBackendServiceRole = new ApplicationBackendServiceRole();
        BeanUtils.copyProperties(to, applicationBackendServiceRole, ConverterUtils.getNullPropertyNames(to));
        applicationBackendServiceRole.setId(new ApplicationBackendServiceRoleId(
                applicationRoleConverter.reverse(to.getId().getApplicationRole()),
                backendServiceRoleConverter.reverse(to.getId().getBackendServiceRole())
        ));
        return applicationBackendServiceRole;
    }

}
