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
import com.biit.server.security.IUserOrganizationProvider;
import com.biit.server.security.model.IUserOrganization;
import com.biit.usermanager.core.converters.RoleConverter;
import com.biit.usermanager.core.converters.models.RoleConverterRequest;
import com.biit.usermanager.core.exceptions.RoleNotFoundException;
import com.biit.usermanager.core.kafka.RoleEventSender;
import com.biit.usermanager.core.kafka.UserEventSender;
import com.biit.usermanager.core.providers.RoleProvider;
import com.biit.usermanager.core.providers.UserApplicationBackendServiceRoleProvider;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.persistence.entities.Role;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

@Controller
public class RoleController extends KafkaElementController<Role, String, RoleDTO, RoleRepository,
        RoleProvider, RoleConverterRequest, RoleConverter> {

    private final UserApplicationBackendServiceRoleProvider userApplicationBackendServiceRoleProvider;

    private final UserEventSender userEventSender;

    @Autowired
    protected RoleController(RoleProvider provider, RoleConverter converter, RoleEventSender eventSender,
                             UserApplicationBackendServiceRoleProvider userApplicationBackendServiceRoleProvider,
                             UserEventSender userEventSender, List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProvider) {
        super(provider, converter, eventSender, userOrganizationProvider);
        this.userApplicationBackendServiceRoleProvider = userApplicationBackendServiceRoleProvider;
        this.userEventSender = userEventSender;
    }

    @Override
    protected RoleConverterRequest createConverterRequest(Role entity) {
        return new RoleConverterRequest(entity);
    }

    public RoleDTO getByName(String name) {
        return getConverter().convert(new RoleConverterRequest(getProvider().findByName(name).orElseThrow(() -> new RoleNotFoundException(this.getClass(),
                "No Role with name '" + name + "' found on the system."))));
    }


    @Override
    @Transactional
    public void delete(RoleDTO entity, String deletedBy) {
        //Send events.
        final Set<UserApplicationBackendServiceRole> usersToInform = userApplicationBackendServiceRoleProvider
                .findByApplicationRoleName(entity.getName());

        super.delete(entity, deletedBy);

        userEventSender.sendEvents(usersToInform, UserEventSender.REVOCATION_EVENT_TAG, deletedBy);
    }

    @Override
    public void deleteById(String id, String deletedBy) {
        //Send events.
        final Set<UserApplicationBackendServiceRole> usersToInform = userApplicationBackendServiceRoleProvider
                .findByApplicationRoleName(id);

        super.deleteById(id, deletedBy);

        userEventSender.sendEvents(usersToInform, UserEventSender.REVOCATION_EVENT_TAG, deletedBy);
    }

    @Override
    public void deleteAll(String deletedBy) {
        //Send events.
        final List<UserApplicationBackendServiceRole> usersToInform = userApplicationBackendServiceRoleProvider.getAll();

        super.deleteAll(deletedBy);

        userEventSender.sendEvents(usersToInform, UserEventSender.REVOCATION_EVENT_TAG, deletedBy);
    }
}
