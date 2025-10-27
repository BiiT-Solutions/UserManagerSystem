package com.biit.usermanager.client.providers;

/*-
 * #%L
 * User Manager System (Test)
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

import com.biit.server.security.IUserOrganizationProvider;
import com.biit.usermanager.dto.OrganizationDTO;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Primary
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserOrganizationProvider implements IUserOrganizationProvider<OrganizationDTO> {

    @Override
    public OrganizationDTO findByName(String name) {
        return null;
    }

    @Override
    public Collection<OrganizationDTO> findByUsername(String username) {
        return List.of();
    }

    @Override
    public Collection<OrganizationDTO> findByUserUID(String uid) {
        return List.of();
    }
}
