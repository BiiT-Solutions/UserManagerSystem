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
import com.biit.usermanager.core.converters.models.ApplicationConverterRequest;
import com.biit.usermanager.core.converters.models.ApplicationRoleConverterRequest;
import com.biit.usermanager.core.converters.models.RoleConverterRequest;
import com.biit.usermanager.dto.ApplicationRoleDTO;
import com.biit.usermanager.dto.ApplicationRoleIdDTO;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.ApplicationRoleId;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRoleConverter extends ElementConverter<ApplicationRole, ApplicationRoleDTO, ApplicationRoleConverterRequest> {
    private final RoleConverter roleConverter;

    private final ApplicationConverter applicationConverter;


    public ApplicationRoleConverter(RoleConverter roleConverter, ApplicationConverter applicationConverter) {
        this.roleConverter = roleConverter;
        this.applicationConverter = applicationConverter;
    }


    @Override
    protected ApplicationRoleDTO convertElement(ApplicationRoleConverterRequest from) {
        final ApplicationRoleDTO applicationRoleDTO = new ApplicationRoleDTO();
        BeanUtils.copyProperties(from.getEntity(), applicationRoleDTO, ConverterUtils.getNullPropertyNames(from.getEntity()));
        applicationRoleDTO.setId(new ApplicationRoleIdDTO(
                        applicationConverter.convert(new ApplicationConverterRequest(from.getEntity().getId().getApplication())),
                        roleConverter.convert(new RoleConverterRequest(from.getEntity().getId().getRole()))
                )
        );
        return applicationRoleDTO;
    }

    @Override
    public ApplicationRole reverse(ApplicationRoleDTO to) {
        if (to == null) {
            return null;
        }
        final ApplicationRole applicationRole = new ApplicationRole();
        BeanUtils.copyProperties(to, applicationRole, ConverterUtils.getNullPropertyNames(to));
        applicationRole.setId(new ApplicationRoleId(
                applicationConverter.reverse(to.getId().getApplication()),
                roleConverter.reverse(to.getId().getRole())
        ));
        return applicationRole;
    }
}
