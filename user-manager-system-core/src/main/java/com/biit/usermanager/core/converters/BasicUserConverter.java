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
import com.biit.usermanager.core.converters.models.BasicUserConverterRequest;
import com.biit.usermanager.dto.BasicUserDTO;
import com.biit.usermanager.persistence.entities.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class BasicUserConverter extends ElementConverter<User, BasicUserDTO, BasicUserConverterRequest> {


    @Override
    protected BasicUserDTO convertElement(BasicUserConverterRequest from) {
        final BasicUserDTO userDTO = new BasicUserDTO();
        BeanUtils.copyProperties(from.getEntity(), userDTO, ConverterUtils.getNullPropertyNames(from.getEntity()));
        if (from.getEntity().getLocale() != null) {
            userDTO.setLocale(Locale.forLanguageTag(from.getEntity().getLocale().replace("_", "-")));
        }
        userDTO.setUUID(from.getEntity().getUuid());
        return userDTO;
    }

    @Override
    public User reverse(BasicUserDTO to) {
        throw new UnsupportedOperationException("Users cannot be created from here.");
    }
}
