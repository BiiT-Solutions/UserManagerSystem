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
import com.biit.usermanager.persistence.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamProvider extends ElementProvider<Team, Long, TeamRepository> {

    @Autowired
    public TeamProvider(TeamRepository repository) {
        super(repository);
    }

    public List<Team> findByParent(Team parent) {
        return getRepository().findByParent(parent);
    }

    public List<Team> findByParentIsNull() {
        return getRepository().findByParentIsNull();
    }

    public List<Team> findByParentIsNotNull() {
        return getRepository().findByParentIsNotNull();
    }

    public List<Team> findByOrganization(Organization organization) {
        return getRepository().findByOrganization(organization);
    }

    public Optional<Team> findByNameAndOrganization(String name, Organization organization) {
        return getRepository().findByNameIgnoreCaseAndOrganization(name, organization);
    }

    public int deleteByName(String name, Organization organization) {
        return getRepository().deleteByNameIgnoreCaseAndOrganization(name, organization);
    }

    public void deleteByOrganization(Organization organization) {
        getRepository().deleteByOrganization(organization);
    }
}
