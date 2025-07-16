package com.biit.usermanager.core.providers;

import com.biit.server.exceptions.InvalidPageSizeException;
import com.biit.server.providers.StorableObjectProvider;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.persistence.entities.TeamMember;
import com.biit.usermanager.persistence.entities.TeamMemberId;
import com.biit.usermanager.persistence.repositories.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamMemberProvider extends StorableObjectProvider<TeamMember, TeamMemberId, TeamMemberRepository> {


    @Autowired
    public TeamMemberProvider(TeamMemberRepository repository) {
        super(repository);
    }

    public Set<TeamMember> findByIdUserId(Long userId) {
        return getRepository().findByIdUserId(userId);
    }

    public List<TeamMember> findByIdUserGroupId(Long teamId) {
        return findByIdUserGroupId(teamId, 0, DEFAULT_PAGE_SIZE);
    }

    public List<TeamMember> findByIdUserGroupId(Long teamId, int page, int size) {
        if (size > MAX_PAGE_SIZE) {
            throw new InvalidPageSizeException(this.getClass(), "Page size is too large. Máx allowed page size is '"
                    + MAX_PAGE_SIZE + "'.");
        }
        final Pageable pageable = PageRequest.of(page, size);
        return getRepository().findByIdTeamId(teamId, pageable).getContent();
    }

    public Set<TeamMember> findByOrganizationName(String organizationName) {
        return getRepository().findByOrganizationName(organizationName);
    }

    public List<TeamMember> findByOrganizationNameIn(Collection<String> organizationName, int page, int size) {
        final Pageable pageable = PageRequest.of(page, size);
        return getRepository().findByOrganizationNameIn(organizationName.stream().map(String::toLowerCase).collect(Collectors.toSet()), pageable).getContent();
    }

    public TeamMember assign(Long userId, Long teamId, String assignedBy) {
        UserManagerLogger.debug(this.getClass(), "Assigning team '{}' to user '{}'", teamId, userId);
        return save(new TeamMember(teamId, userId, assignedBy));
    }

    public long countByIdUserGroupId(Long teamId) {
        return getRepository().countByIdTeamId(teamId);
    }

    public long countByOrganization(String organization) {
        return getRepository().countByOrganizationName(organization);
    }

    public void deleteByOrganizationName(String organizationName) {
        getRepository().deleteByOrganizationName(organizationName);
    }

    public void deleteByTeam(Long teamId) {
        getRepository().deleteByIdTeamId(teamId);
    }
}
