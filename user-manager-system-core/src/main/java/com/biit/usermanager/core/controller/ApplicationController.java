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
import com.biit.usermanager.core.converters.ApplicationConverter;
import com.biit.usermanager.core.converters.models.ApplicationConverterRequest;
import com.biit.usermanager.core.kafka.ApplicationEventSender;
import com.biit.usermanager.core.kafka.UserEventSender;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.core.providers.UserApplicationBackendServiceRoleProvider;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.repositories.ApplicationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

@Controller
public class ApplicationController extends KafkaElementController<Application, String, ApplicationDTO, ApplicationRepository,
        ApplicationProvider, ApplicationConverterRequest, ApplicationConverter> {

    private final UserApplicationBackendServiceRoleProvider userApplicationBackendServiceRoleProvider;

    private final UserEventSender userEventSender;

    @Autowired
    protected ApplicationController(ApplicationProvider provider, ApplicationConverter converter, ApplicationEventSender eventSender,
                                    UserApplicationBackendServiceRoleProvider userApplicationBackendServiceRoleProvider,
                                    UserEventSender userEventSender, List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProvider) {
        super(provider, converter, eventSender, userOrganizationProvider);
        this.userApplicationBackendServiceRoleProvider = userApplicationBackendServiceRoleProvider;
        this.userEventSender = userEventSender;
    }

    @Override
    protected ApplicationConverterRequest createConverterRequest(Application entity) {
        return new ApplicationConverterRequest(entity);
    }

    public ApplicationDTO getByName(String applicationName) {
        return getConverter().convert(new ApplicationConverterRequest(getProvider().findByName(applicationName).orElse(null)));
    }


    @Override
    @Transactional
    public void delete(ApplicationDTO entity, String deletedBy) {
        //Send events.
        final Set<UserApplicationBackendServiceRole> usersToInform = userApplicationBackendServiceRoleProvider.findByApplicationName(entity.getName());

        super.delete(entity, deletedBy);

        userEventSender.sendEvents(usersToInform, UserEventSender.REVOCATION_EVENT_TAG, deletedBy);
    }

    @Override
    public void deleteById(String name, String deletedBy) {
        //Send events.
        final Set<UserApplicationBackendServiceRole> usersToInform = userApplicationBackendServiceRoleProvider.findByApplicationName(name);

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
