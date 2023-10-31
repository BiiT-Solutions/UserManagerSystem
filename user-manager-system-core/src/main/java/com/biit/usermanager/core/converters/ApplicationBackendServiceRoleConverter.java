package com.biit.usermanager.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.server.converters.ConverterUtils;
import com.biit.usermanager.core.converters.models.ApplicationBackendServiceRoleConverterRequest;
import com.biit.usermanager.core.converters.models.ApplicationRoleConverterRequest;
import com.biit.usermanager.core.converters.models.BackendServiceRoleConverterRequest;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.core.providers.ApplicationRoleProvider;
import com.biit.usermanager.core.providers.BackendServiceProvider;
import com.biit.usermanager.core.providers.BackendServiceRoleProvider;
import com.biit.usermanager.core.providers.RoleProvider;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleDTO;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleIdDTO;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRoleId;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.Role;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class ApplicationBackendServiceRoleConverter extends ElementConverter<ApplicationBackendServiceRole,
        ApplicationBackendServiceRoleDTO, ApplicationBackendServiceRoleConverterRequest> {

    private final ApplicationRoleConverter applicationRoleConverter;
    private final BackendServiceRoleConverter backendServiceRoleConverter;

    private final ApplicationRoleProvider applicationRoleProvider;

    private final BackendServiceRoleProvider backendServiceRoleProvider;

    private final ApplicationProvider applicationProvider;

    private final RoleProvider roleProvider;

    private final BackendServiceProvider backendServiceProvider;

    public ApplicationBackendServiceRoleConverter(ApplicationRoleConverter applicationRoleConverter,
                                                  BackendServiceRoleConverter backendServiceRoleConverter,
                                                  ApplicationRoleProvider applicationRoleProvider,
                                                  BackendServiceRoleProvider backendServiceRoleProvider,
                                                  ApplicationProvider applicationProvider,
                                                  RoleProvider roleProvider,
                                                  BackendServiceProvider backendServiceProvider) {
        this.applicationRoleConverter = applicationRoleConverter;
        this.backendServiceRoleConverter = backendServiceRoleConverter;
        this.applicationRoleProvider = applicationRoleProvider;
        this.backendServiceRoleProvider = backendServiceRoleProvider;
        this.applicationProvider = applicationProvider;
        this.roleProvider = roleProvider;
        this.backendServiceProvider = backendServiceProvider;
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
    public List<ApplicationBackendServiceRoleDTO> convertAll(Collection<ApplicationBackendServiceRoleConverterRequest> from) {
        if (from == null) {
            return new ArrayList<>();
        }

        final List<ApplicationBackendServiceRoleDTO> dtos = new ArrayList<>();

        final Map<String, Application> applications = new HashMap<>();
        final Set<String> missingApplicationIds = new HashSet<>();
        final Map<String, Role> roles = new HashMap<>();
        final Set<String> missingRolesIds = new HashSet<>();
        final Map<String, BackendService> backendServices = new HashMap<>();
        final Set<String> missingBackendServicesIds = new HashSet<>();

        from.forEach(applicationBackendServiceRoleConverterRequest -> {
            final ApplicationRole applicationRole = applicationBackendServiceRoleConverterRequest.getEntity().getId().getApplicationRole();
            if (!Hibernate.isInitialized(applicationRole)) {
                missingApplicationIds.add(applicationRole.getId().getApplication().getName());
                missingRolesIds.add(applicationRole.getId().getRole().getName());
            } else {
                applications.put(applicationRole.getId().getApplication().getName(), applicationRole.getId().getApplication());
                roles.put(applicationRole.getId().getRole().getName(), applicationRole.getId().getRole());
            }

            final BackendServiceRole backendServiceRole = applicationBackendServiceRoleConverterRequest.getEntity().getId().getBackendServiceRole();
            if (!Hibernate.isInitialized(backendServiceRole)) {
                missingBackendServicesIds.add(backendServiceRole.getId().getBackendService().getName());
            } else {
                backendServices.put(backendServiceRole.getId().getBackendService().getName(), backendServiceRole.getBackendService());
            }
        });


        //Get all missing entities.
        missingApplicationIds.removeAll(applications.keySet());
        missingRolesIds.removeAll(roles.keySet());
        missingBackendServicesIds.removeAll(backendServices.keySet());

        final List<Application> retrievedApplications = applicationProvider.findByIdIn(missingApplicationIds);
        retrievedApplications.forEach(application -> applications.put(application.getId(), application));

        final List<Role> retrievedRoles = roleProvider.findByIdIn(missingRolesIds);
        retrievedRoles.forEach(role -> roles.put(role.getId(), role));

        final List<BackendService> retrievedBackendServices = backendServiceProvider.findByIdIn(missingBackendServicesIds);
        retrievedBackendServices.forEach(backendService -> backendServices.put(backendService.getId(), backendService));

        from.forEach(applicationBackendServiceRoleConverterRequest -> {
            final ApplicationBackendServiceRoleDTO applicationBackendServiceRoleDTO = new ApplicationBackendServiceRoleDTO();
            BeanUtils.copyProperties(applicationBackendServiceRoleConverterRequest.getEntity(), applicationBackendServiceRoleDTO,
                    ConverterUtils.getNullPropertyNames(applicationBackendServiceRoleConverterRequest.getEntity()));

            applicationBackendServiceRoleDTO.setId(new ApplicationBackendServiceRoleIdDTO(
                    applicationRoleConverter.convertElement(new ApplicationRoleConverterRequest(
                            new ApplicationRole(
                                    applications.get(applicationBackendServiceRoleConverterRequest.getEntity().getId()
                                            .getApplicationRole().getId().getApplication().getName()),
                                    roles.get(applicationBackendServiceRoleConverterRequest.getEntity().getId()
                                            .getApplicationRole().getId().getRole().getName())
                            )
                    )),
                    backendServiceRoleConverter.convertElement(new BackendServiceRoleConverterRequest(
                            new BackendServiceRole(
                                    backendServices.get(applicationBackendServiceRoleConverterRequest.getEntity().getId()
                                            .getBackendServiceRole().getId().getBackendService().getName()),
                                    applicationBackendServiceRoleConverterRequest.getEntity().getId().getBackendServiceRole().getId().getName()
                            )
                    )))
            );
            dtos.add(applicationBackendServiceRoleDTO);
        });

        return dtos;
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
