package com.biit.usermanager.core.kafka;

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


import com.biit.kafka.events.EventSender;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.usermanager.core.utils.EventTags;
import com.biit.usermanager.dto.ApplicationRoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRolesEventSender extends EventSender<ApplicationRoleDTO> {

    private static final String EVENT_TYPE = "applicationRoles";


    public ApplicationRolesEventSender(@Autowired(required = false) KafkaEventTemplate kafkaTemplate) {
        super(kafkaTemplate, EventTags.ROLE, EVENT_TYPE);
    }
}
