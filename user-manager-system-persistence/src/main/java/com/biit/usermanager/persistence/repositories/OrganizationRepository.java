package com.biit.usermanager.persistence.repositories;

/*-
 * #%L
 * User Manager System (Persistence)
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

import com.biit.server.persistence.repositories.ElementRepository;
import com.biit.usermanager.persistence.entities.Organization;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public interface OrganizationRepository extends ElementRepository<Organization, String> {

    Optional<Organization> findByNameIgnoreCase(String name);

    @Query("""
            SELECT o FROM Organization o WHERE o.name=
            (SELECT t.organization.name FROM Team t WHERE t.id=:teamId)
            """)
    Optional<Organization> findByTeam(Long teamId);

    @Query("""
            SELECT DISTINCT o, tm.createdAt FROM Organization o
                        RIGHT JOIN Team t ON t.organization.id=o.id
                                    RIGHT JOIN TeamMember tm ON t.id=tm.id.teamId WHERE tm.id.userId=:userId
                                                ORDER BY tm.createdAt DESC
            """)
    Set<Organization> findByUser(Long userId);
}
