package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.StorableObjectRepository;
import com.biit.usermanager.persistence.entities.TeamMember;
import com.biit.usermanager.persistence.entities.TeamMemberId;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface TeamMemberRepository extends StorableObjectRepository<TeamMember,
        TeamMemberId> {

    Set<TeamMember> findByIdTeamId(Long teamId);

    Set<TeamMember> findByIdUserId(Long userId);

    @Query("""
            SELECT tm FROM TeamMember tm WHERE tm.id.teamId IN
            (SELECT t.id FROM Team t WHERE t.organization.name=:organizationName)
            """)
    Set<TeamMember> findByOrganizationName(String organizationName);
}
