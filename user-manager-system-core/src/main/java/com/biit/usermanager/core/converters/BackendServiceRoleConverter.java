package com.biit.usermanager.core.converters;

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

import com.biit.server.controller.converters.ElementConverter;
import com.biit.server.converters.ConverterUtils;
import com.biit.usermanager.core.converters.models.BackendServiceConverterRequest;
import com.biit.usermanager.core.converters.models.BackendServiceRoleConverterRequest;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import com.biit.usermanager.dto.BackendServiceRoleIdDTO;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.BackendServiceRoleId;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class BackendServiceRoleConverter extends ElementConverter<BackendServiceRole, BackendServiceRoleDTO, BackendServiceRoleConverterRequest> {

    private final BackendServiceConverter backendServiceConverter;

    public BackendServiceRoleConverter(BackendServiceConverter backendServiceConverter) {
        this.backendServiceConverter = backendServiceConverter;
    }

    @Override
    protected BackendServiceRoleDTO convertElement(BackendServiceRoleConverterRequest from) {
        final BackendServiceRoleDTO backendServiceRoleDTO = new BackendServiceRoleDTO();
        BeanUtils.copyProperties(from.getEntity(), backendServiceRoleDTO, ConverterUtils.getNullPropertyNames(from.getEntity()));

        backendServiceRoleDTO.setId(new BackendServiceRoleIdDTO(backendServiceConverter.convertElement(
                new BackendServiceConverterRequest(from.getEntity().getId().getBackendService())), from.getEntity().getId().getName()));

        return backendServiceRoleDTO;
    }

    @Override
    public BackendServiceRole reverse(BackendServiceRoleDTO to) {
        if (to == null) {
            return null;
        }
        final BackendServiceRole backendServiceRole = new BackendServiceRole();
        BeanUtils.copyProperties(to, backendServiceRole, ConverterUtils.getNullPropertyNames(to));

        backendServiceRole.setId(new BackendServiceRoleId(backendServiceConverter.reverse(to.getId().getBackendService()), to.getId().getName()));

        return backendServiceRole;
    }
}
