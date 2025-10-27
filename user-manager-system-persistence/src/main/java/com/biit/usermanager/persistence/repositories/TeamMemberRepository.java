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

import com.biit.server.persistence.repositories.StorableObjectRepository;
import com.biit.usermanager.persistence.entities.TeamMember;
import com.biit.usermanager.persistence.entities.TeamMemberId;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
@Transactional
public interface TeamMemberRepository extends StorableObjectRepository<TeamMember,
        TeamMemberId> {

    Page<TeamMember> findByIdTeamId(Long teamId, Pageable pageable);

    void deleteByIdTeamId(Long teamId);

    long countByIdTeamId(Long teamId);

    Set<TeamMember> findByIdUserId(Long userId);

    @Query("""
            SELECT tm FROM TeamMember tm WHERE tm.id.teamId IN
            (SELECT t.id FROM Team t WHERE lower(t.organization.name) = lower(:organizationName))
            """)
    Set<TeamMember> findByOrganizationName(String organizationName);

    @Query("""
            SELECT tm FROM TeamMember tm WHERE tm.id.teamId IN
            (SELECT t.id FROM Team t WHERE lower(t.organization.name) IN :organizationNames)
            """)
    Page<TeamMember> findByOrganizationNameIn(Collection<String> organizationNames, Pageable pageable);

    @Query("""
            SELECT COUNT(tm) FROM TeamMember tm WHERE tm.id.teamId IN
            (SELECT t.id FROM Team t WHERE lower(t.organization.name) = lower(:organizationName))
            """)
    long countByOrganizationName(String organizationName);


    @Modifying
    @Query("""
            DELETE FROM TeamMember tm WHERE tm.id.teamId IN
            (SELECT t.id FROM Team t WHERE lower(t.organization.name) = lower(:organizationName))
            """)
    void deleteByOrganizationName(String organizationName);
}
