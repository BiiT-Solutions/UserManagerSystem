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
import com.biit.usermanager.core.converters.models.UserGroupConverterRequest;
import com.biit.usermanager.dto.UserGroupDTO;
import com.biit.usermanager.persistence.entities.UserGroup;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserGroupConverter extends ElementConverter<UserGroup, UserGroupDTO, UserGroupConverterRequest> {

    @Override
    protected UserGroupDTO convertElement(UserGroupConverterRequest from) {
        if (from.getEntity() == null) {
            return null;
        }
        final UserGroupDTO userGroupDTO = new UserGroupDTO();
        BeanUtils.copyProperties(from.getEntity(), userGroupDTO, ConverterUtils.getNullPropertyNames(from.getEntity()));
        return userGroupDTO;
    }

    @Override
    public UserGroup reverse(UserGroupDTO to) {
        if (to == null) {
            return null;
        }
        final UserGroup userGroup = new UserGroup();
        BeanUtils.copyProperties(to, userGroup, ConverterUtils.getNullPropertyNames(to));
        return userGroup;
    }
}
