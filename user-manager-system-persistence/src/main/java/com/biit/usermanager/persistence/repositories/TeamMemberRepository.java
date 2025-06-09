package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.StorableObjectRepository;
import com.biit.usermanager.persistence.entities.TeamMember;
import com.biit.usermanager.persistence.entities.TeamMemberId;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@Transactional
public interface TeamMemberRepository extends StorableObjectRepository<TeamMember,
        TeamMemberId> {

    Page<TeamMember> findByIdTeamId(Long teamId, Pageable pageable);

    Set<TeamMember> findByIdUserId(Long userId);

    @Query("""
            SELECT tm FROM TeamMember tm WHERE tm.id.teamId IN
            (SELECT t.id FROM Team t WHERE lower(t.organization.name) = lower(:organizationName))
            """)
    Set<TeamMember> findByOrganizationName(String organizationName);
}
