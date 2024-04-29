package com.biit.usermanager.core.controller;


import com.biit.kafka.controllers.KafkaElementController;
import com.biit.usermanager.core.converters.OrganizationConverter;
import com.biit.usermanager.core.converters.models.OrganizationConverterRequest;
import com.biit.usermanager.core.exceptions.OrganizationNotFoundException;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.kafka.OrganizationEventSender;
import com.biit.usermanager.core.providers.OrganizationProvider;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.dto.OrganizationDTO;
import com.biit.usermanager.persistence.entities.Organization;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrganizationController extends KafkaElementController<Organization, String, OrganizationDTO, OrganizationRepository,
        OrganizationProvider, OrganizationConverterRequest, OrganizationConverter> {

    @Value("${spring.application.name}")
    private String applicationName;

    private final UserProvider userProvider;

    @Autowired
    protected OrganizationController(OrganizationProvider provider, OrganizationConverter converter, OrganizationEventSender eventSender,
                                     UserProvider userProvider) {
        super(provider, converter, eventSender);
        this.userProvider = userProvider;
    }

    @Override
    protected OrganizationConverterRequest createConverterRequest(Organization entity) {
        return new OrganizationConverterRequest(entity);
    }

    public OrganizationDTO getByName(String name) {
        return getConverter().convert(new OrganizationConverterRequest(getProvider().findByName(name).orElseThrow(
                () -> new OrganizationNotFoundException(this.getClass(), "No Organization with name '" + name + "' found on the system."))));
    }

    public List<OrganizationDTO> getByUser(Long userId) {
        final User user = userProvider.findById(userId).orElseThrow(()
                -> new UserNotFoundException(this.getClass(), "No user exists with id '" + userId + "'."));

        return getConverter().convertAll(getProvider().findByUser(user).stream()
                .map(this::createConverterRequest).collect(Collectors.toSet()));
    }
}
