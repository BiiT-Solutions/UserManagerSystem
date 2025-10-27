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
import com.biit.usermanager.core.converters.models.RoleConverterRequest;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.persistence.entities.Role;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter extends ElementConverter<Role, RoleDTO, RoleConverterRequest> {


    @Override
    protected RoleDTO convertElement(RoleConverterRequest from) {
        final RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(from.getEntity(), roleDTO, ConverterUtils.getNullPropertyNames(from.getEntity()));
        return roleDTO;
    }

    @Override
    public Role reverse(RoleDTO to) {
        if (to == null) {
            return null;
        }
        final Role role = new Role();
        BeanUtils.copyProperties(to, role, ConverterUtils.getNullPropertyNames(to));
        return role;
    }
}
