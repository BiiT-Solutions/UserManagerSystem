package com.biit.usermanager.core.providers;

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

import com.biit.server.providers.ElementProvider;
import com.biit.usermanager.persistence.entities.Organization;
import com.biit.usermanager.persistence.entities.Team;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class OrganizationProvider extends ElementProvider<Organization, String, OrganizationRepository> {

    @Autowired
    public OrganizationProvider(OrganizationRepository repository) {
        super(repository);
    }

    public Optional<Organization> findByName(String name) {
        return getRepository().findByNameIgnoreCase(name);
    }

    public Optional<Organization> findByTeam(Team team) {
        return getRepository().findByTeam(team.getId());
    }

    public Set<Organization> findByUser(User user) {
        return getRepository().findByUser(user.getId());
    }
}
