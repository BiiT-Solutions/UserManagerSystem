package com.biit.usermanager.core.providers;

import com.biit.server.providers.StorableObjectProvider;
import com.biit.usermanager.persistence.entities.TeamMember;
import com.biit.usermanager.persistence.entities.TeamMemberId;
import com.biit.usermanager.persistence.repositories.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TeamMemberProvider extends StorableObjectProvider<TeamMember, TeamMemberId, TeamMemberRepository> {


    @Autowired
    public TeamMemberProvider(TeamMemberRepository repository) {
        super(repository);
    }

    public Set<TeamMember> findByIdUserId(Long userId) {
        return getRepository().findByIdUserId(userId);
    }

    public Set<TeamMember> findByIdUserGroupId(Long teamId) {
        return getRepository().findByIdTeamId(teamId);
    }

    public Set<TeamMember> findByOrganizationName(String organizationName) {
        return getRepository().findByOrganizationName(organizationName);
    }

    public TeamMember assign(Long userId, Long teamId) {
        return save(new TeamMember(teamId, userId));
    }
}
