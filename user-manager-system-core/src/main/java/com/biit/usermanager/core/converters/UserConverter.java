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
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.persistence.entities.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class UserConverter extends ElementConverter<User, UserDTO, UserConverterRequest> {

    @Value("${bcrypt.salt:}")
    private String bcryptSalt;


    @Override
    protected UserDTO convertElement(UserConverterRequest from) {
        final UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(from.getEntity(), userDTO, ConverterUtils.getNullPropertyNames(from.getEntity()));
        if (from.getEntity().getLocale() != null) {
            userDTO.setLocale(Locale.forLanguageTag(from.getEntity().getLocale().replace("_", "-")));
        }
        userDTO.setUUID(from.getEntity().getUuid());
        if (from.getEntity().getPassword().startsWith(bcryptSalt)) {
            userDTO.setPassword(from.getEntity().getPassword().substring(bcryptSalt.length()));
        } else {
            userDTO.setPassword(from.getEntity().getPassword());
        }
        return userDTO;
    }

    @Override
    public User reverse(UserDTO to) {
        if (to == null) {
            return null;
        }
        final User user = new User();
        BeanUtils.copyProperties(to, user, ConverterUtils.getNullPropertyNames(to));
        if (to.getLocale() != null) {
            user.setLocale(to.getLocale().toLanguageTag().replace("-", "_"));
        }
        if (to.getPassword() != null && !to.getPassword().isBlank()) {
            user.setPassword(bcryptSalt + to.getPassword());
        }
        user.setUuid(to.getUUID());
        if (to.getUsername() != null) {
            user.setUsername(to.getUsername().toLowerCase());
        } else {
            user.setUsername(to.getEmail().toLowerCase());
        }
        return user;
    }
}
