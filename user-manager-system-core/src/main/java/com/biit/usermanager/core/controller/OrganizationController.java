package com.biit.usermanager.core.controller;

/*-
 * #%L
 * User Manager System (core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import com.biit.kafka.controllers.KafkaElementController;
import com.biit.server.logger.DtoControllerLogger;
import com.biit.server.security.IUserOrganizationProvider;
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
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@Order(1)
@Qualifier("organizationController")
public class OrganizationController extends KafkaElementController<Organization, String, OrganizationDTO, OrganizationRepository,
        OrganizationProvider, OrganizationConverterRequest, OrganizationConverter> implements IUserOrganizationProvider<OrganizationDTO> {

    private final UserProvider userProvider;
    private final TeamProvider teamProvider;
    private final TeamMemberProvider teamMemberProvider;

    @Autowired
    protected OrganizationController(OrganizationProvider provider, OrganizationConverter converter, OrganizationEventSender eventSender,
                                     UserProvider userProvider, TeamProvider teamProvider, TeamMemberProvider teamMemberProvider) {
        //I cannot add myself, so I add it on the @PostConstruct
        super(provider, converter, eventSender, new ArrayList<>());
        this.userProvider = userProvider;
        this.teamProvider = teamProvider;
        this.teamMemberProvider = teamMemberProvider;
    }

    @PostConstruct
    public void init() {
        setUserOrganizationProvider(this);
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

    @Override
    public OrganizationDTO findByName(String name) {
        return getConverter().convert(new OrganizationConverterRequest(getProvider().findByName(name).orElseThrow(
                () -> new OrganizationNotFoundException(this.getClass(), "No Organization with name '" + name + "' found on the system."))));
    }

    public List<OrganizationDTO> findByUserId(Long userId) {
        final User user = userProvider.findById(userId).orElseThrow(()
                -> new UserNotFoundException(this.getClass(), "No user exists with id '" + userId + "'."));

        return findByUser(user);
    }

    @Override
    public List<OrganizationDTO> findByUsername(String username) {
        final User user = userProvider.findByUsername(username).orElseThrow(()
                -> new UserNotFoundException(this.getClass(), "No user exists with username '" + username + "'."));

        return findByUser(user);
    }

    public List<OrganizationDTO> findByUser(User user) {
        return getConverter().convertAll(getProvider().findByUser(user).stream()
                .map(this::createConverterRequest).collect(Collectors.toSet()));
    }


    @Transactional
    @Override
    public void delete(OrganizationDTO entity, String deletedBy) {
        DtoControllerLogger.info(this.getClass(), "Entity '{}' deleted by '{}'.", entity, deletedBy);
        teamMemberProvider.deleteByOrganizationName(entity.getName());
        teamProvider.deleteByOrganization(reverse(entity));
        getProvider().delete(this.getConverter().reverse(entity));
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


    @Override
    public Collection<OrganizationDTO> findByUserUID(String uid) {
        final User user = userProvider.findByUuid(UUID.fromString(uid)).orElseThrow(()
                -> new UserNotFoundException(this.getClass(), "No user exists with id '" + uid + "'."));
        return findByUser(user);
    }
}
