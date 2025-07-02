package com.biit.usermanager.core.controller;


import com.biit.kafka.controllers.KafkaElementController;
import com.biit.server.logger.DtoControllerLogger;
import com.biit.usermanager.core.converters.OrganizationConverter;
import com.biit.usermanager.core.converters.models.OrganizationConverterRequest;
import com.biit.usermanager.core.exceptions.OrganizationAlreadyExistsException;
import com.biit.usermanager.core.exceptions.OrganizationNotFoundException;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.kafka.OrganizationEventSender;
import com.biit.usermanager.core.providers.OrganizationProvider;
import com.biit.usermanager.core.providers.TeamMemberProvider;
import com.biit.usermanager.core.providers.TeamProvider;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.dto.OrganizationDTO;
import com.biit.usermanager.persistence.entities.Organization;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.OrganizationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class OrganizationController extends KafkaElementController<Organization, String, OrganizationDTO, OrganizationRepository,
        OrganizationProvider, OrganizationConverterRequest, OrganizationConverter> {

    private final UserProvider userProvider;
    private final TeamProvider teamProvider;
    private final TeamMemberProvider teamMemberProvider;

    @Autowired
    protected OrganizationController(OrganizationProvider provider, OrganizationConverter converter, OrganizationEventSender eventSender,
                                     UserProvider userProvider, TeamProvider teamProvider, TeamMemberProvider teamMemberProvider) {
        super(provider, converter, eventSender);
        this.userProvider = userProvider;
        this.teamProvider = teamProvider;
        this.teamMemberProvider = teamMemberProvider;
    }

    @Override
    protected OrganizationConverterRequest createConverterRequest(Organization entity) {
        return new OrganizationConverterRequest(entity);
    }

    @Override
    public OrganizationDTO create(OrganizationDTO dto, String creatorName) {
        if (getProvider().findByName(dto.getName()).isPresent()) {
            throw new OrganizationAlreadyExistsException(this.getClass(), "Already exists an organization with name '" + dto.getName() + "'.");
        }
        return super.create(dto, creatorName);
    }

    public OrganizationDTO getByName(String name) {
        return getConverter().convert(new OrganizationConverterRequest(getProvider().findByName(name).orElseThrow(
                () -> new OrganizationNotFoundException(this.getClass(), "No Organization with name '" + name + "' found on the system."))));
    }

    public List<OrganizationDTO> getByUser(Long userId) {
        final User user = userProvider.findById(userId).orElseThrow(()
                -> new UserNotFoundException(this.getClass(), "No user exists with id '" + userId + "'."));

        return getByUser(user);
    }

    public List<OrganizationDTO> getByUser(String username) {
        final User user = userProvider.findByUsername(username).orElseThrow(()
                -> new UserNotFoundException(this.getClass(), "No user exists with username '" + username + "'."));

        return getByUser(user);
    }

    public List<OrganizationDTO> getByUser(User user) {
        return getConverter().convertAll(getProvider().findByUser(user).stream()
                .map(this::createConverterRequest).collect(Collectors.toSet()));
    }


    @Transactional
    @Override
    public void delete(OrganizationDTO entity, String deletedBy) {
        DtoControllerLogger.info(this.getClass(), "Entity '{}' deleted by '{}'.", entity, deletedBy);
        getProvider().delete(this.getConverter().reverse(entity));
        teamMemberProvider.deleteByOrganizationName(entity.getName());
        teamProvider.deleteByOrganization(reverse(entity));
        getProvider().deleteById(entity.getName());
    }


    @Transactional
    @Override
    public void deleteAll(String deletedBy) {
        DtoControllerLogger.info(this.getClass(), "All Entities deleted by '{}'.", deletedBy);
        final List<OrganizationDTO> organizations = get();
        organizations.forEach(organization ->
                delete(organization, deletedBy));
    }


    @Transactional
    @Override
    public void deleteById(String organizationName, String deletedBy) {
        DtoControllerLogger.warning(this.getClass(), "Deleting entity with id '{}' by '{}'.", organizationName, deletedBy);
        final Optional<Organization> organization = getProvider().findByName(organizationName);
        if (organization.isEmpty()) {
            throw new OrganizationNotFoundException(this.getClass(), "No Organization exists with name '" + organizationName + "'.");
        }
        delete(convert(organization.get()), deletedBy);
    }
}
