package com.biit.usermanager.client.providers.converters;

/*-
 * #%L
 * User Manager System (Rest Client)
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

import com.biit.server.security.CreateUserRequest;
import com.biit.usermanager.dto.UserDTO;

public final class UserDTOConverter {

    private UserDTOConverter() {
    }

    public static UserDTO convert(CreateUserRequest createUserRequest) {
        final UserDTO userDTO = new UserDTO();
        userDTO.setFirstname(createUserRequest.getFirstname());
        userDTO.setLastName(createUserRequest.getLastname());
        userDTO.setUsername(createUserRequest.getUsername());
        userDTO.setExternalReference(createUserRequest.getExternalReference());
        if (createUserRequest.getEmail() != null && !createUserRequest.getEmail().isBlank()) {
            userDTO.setEmail(createUserRequest.getEmail());
        } else if (createUserRequest.getUsername().contains("@") && createUserRequest.getUsername().contains(".")) {
            userDTO.setEmail(createUserRequest.getUsername());
        }
        userDTO.setPassword(createUserRequest.getPassword());
        return userDTO;
    }
}
