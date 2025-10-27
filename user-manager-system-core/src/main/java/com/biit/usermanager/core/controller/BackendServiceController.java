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
import com.biit.usermanager.core.converters.BackendServiceConverter;
import com.biit.usermanager.core.converters.models.BackendServiceConverterRequest;
import com.biit.usermanager.core.exceptions.BackendServiceNotFoundException;
import com.biit.usermanager.core.kafka.BackendServiceEventSender;
import com.biit.usermanager.core.kafka.UserEventSender;
import com.biit.usermanager.core.providers.BackendServiceProvider;
import com.biit.usermanager.core.providers.UserApplicationBackendServiceRoleProvider;
import com.biit.usermanager.dto.BackendServiceDTO;
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.repositories.BackendServiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

@Controller
public class BackendServiceController extends KafkaElementController<BackendService, String, BackendServiceDTO, BackendServiceRepository,
        BackendServiceProvider, BackendServiceConverterRequest, BackendServiceConverter> {

    private final UserApplicationBackendServiceRoleProvider userApplicationBackendServiceRoleProvider;

    private final UserEventSender userEventSender;

    protected BackendServiceController(BackendServiceProvider provider, BackendServiceConverter converter,
                                       BackendServiceEventSender eventSender,
                                       UserApplicationBackendServiceRoleProvider userApplicationBackendServiceRoleProvider,
                                       UserEventSender userEventSender, List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProvider) {
        super(provider, converter, eventSender, userOrganizationProvider);
        this.userApplicationBackendServiceRoleProvider = userApplicationBackendServiceRoleProvider;
        this.userEventSender = userEventSender;
    }

    @Override
    protected BackendServiceConverterRequest createConverterRequest(BackendService backendService) {
        return new BackendServiceConverterRequest(backendService);
    }

    public BackendServiceDTO findByName(String backendServiceName) {
        return convert(getProvider().findByName(backendServiceName).orElseThrow(() ->
                new BackendServiceNotFoundException(this.getClass(), "No backend service with name '" + backendServiceName + "' exists on the system")));
    }

    @Override
    @Transactional
    public void delete(BackendServiceDTO entity, String deletedBy) {
        //Send events.
        final Set<UserApplicationBackendServiceRole> usersToInform = userApplicationBackendServiceRoleProvider
                .findByBackendServiceName(entity.getName());

        super.delete(entity, deletedBy);

        userEventSender.sendEvents(usersToInform, UserEventSender.REVOCATION_EVENT_TAG, deletedBy);
    }

    @Override
    public void deleteById(String name, String deletedBy) {
        //Send events.
        final Set<UserApplicationBackendServiceRole> usersToInform = userApplicationBackendServiceRoleProvider.
                findByBackendServiceName(name);

        super.deleteById(name, deletedBy);

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
