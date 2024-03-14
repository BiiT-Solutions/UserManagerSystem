package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.StorableObjectRepository;
import com.biit.usermanager.persistence.entities.TeamMember;
import com.biit.usermanager.persistence.entities.TeamMemberId;

import java.util.Set;

public interface TeamMemberRepository extends StorableObjectRepository<TeamMember,
        TeamMemberId> {

    Set<TeamMember> findByIdTeamId(Long teamId);

    Set<TeamMember> findByIdUserId(Long userId);
}
