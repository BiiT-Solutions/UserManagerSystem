package com.biit.usermanager.core.controller;


import com.biit.kafka.controllers.KafkaElementController;
import com.biit.server.logger.DtoControllerLogger;
import com.biit.usermanager.core.converters.OrganizationConverter;
import com.biit.usermanager.core.converters.TeamConverter;
import com.biit.usermanager.core.converters.UserConverter;
import com.biit.usermanager.core.converters.models.TeamConverterRequest;
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.core.exceptions.OrganizationAlreadyExistsException;
import com.biit.usermanager.core.exceptions.OrganizationNotFoundException;
import com.biit.usermanager.core.exceptions.TeamNotFoundException;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.kafka.TeamEventSender;
import com.biit.usermanager.core.providers.OrganizationProvider;
import com.biit.usermanager.core.providers.TeamMemberProvider;
import com.biit.usermanager.core.providers.TeamProvider;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.dto.OrganizationDTO;
import com.biit.usermanager.dto.TeamDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.persistence.entities.Organization;
import com.biit.usermanager.persistence.entities.Team;
import com.biit.usermanager.persistence.entities.TeamMember;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.TeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Controller
public class TeamController extends KafkaElementController<Team, Long, TeamDTO, TeamRepository,
        TeamProvider, TeamConverterRequest, TeamConverter> {

    private final OrganizationConverter organizationConverter;
    private final OrganizationProvider organizationProvider;
    private final UserProvider userProvider;

    private final TeamMemberProvider teamMemberProvider;
    private final UserConverter userConverter;

    @Autowired
    protected TeamController(TeamProvider provider, TeamConverter converter, OrganizationConverter organizationConverter,
                             OrganizationProvider organizationProvider, TeamEventSender eventSender, UserProvider userProvider,
                             TeamMemberProvider teamMemberProvider, UserConverter userConverter) {
        super(provider, converter, eventSender);
        this.organizationConverter = organizationConverter;
        this.organizationProvider = organizationProvider;
        this.userProvider = userProvider;
        this.teamMemberProvider = teamMemberProvider;
        this.userConverter = userConverter;
    }

    @Override
    protected TeamConverterRequest createConverterRequest(Team entity) {
        return new TeamConverterRequest(entity);
    }

    @Override
    public TeamDTO create(TeamDTO dto, String creatorName) {
        if (dto.getOrganization() == null) {
            throw new OrganizationNotFoundException(this.getClass(), "Organization cannot be null.");
        }
        if (getProvider().findByNameAndOrganization(dto.getName(), organizationConverter.reverse(dto.getOrganization())).isPresent()) {
            throw new OrganizationAlreadyExistsException(this.getClass(), "Already exists a team with name '" + dto.getName()
                    + "' on organization '" + dto.getOrganization().getName() + "'.");
        }
        return super.create(dto, creatorName);
    }


    public TeamDTO getByName(String name, String organizationName) {
        final Organization organization = organizationProvider.findByName(organizationName).orElseThrow(() ->
                new OrganizationNotFoundException(this.getClass(), "Organization with name '" + organizationName + "' not found."));
        return getConverter().convert(new TeamConverterRequest(getProvider().findByNameAndOrganization(name, organization)
                .orElseThrow(() -> new TeamNotFoundException(this.getClass(), "No Team with name '" + name + "' found on the system.")), organization));
    }


    public TeamDTO getByName(String name, OrganizationDTO organizationDTO) {
        return getConverter().convert(new TeamConverterRequest(getProvider().findByNameAndOrganization(name, organizationConverter.reverse(organizationDTO))
                .orElseThrow(() -> new TeamNotFoundException(this.getClass(), "No Team with name '" + name + "' found on the system."))));
    }


    public List<TeamDTO> getByOrganization(String organizationName) {
        final Organization organization = organizationProvider.findByName(organizationName).orElseThrow(() ->
                new OrganizationNotFoundException(this.getClass(), "Organization with name '" + organizationName + "' not found."));
        return getByOrganization(organization).stream().sorted().toList();
    }


    public List<TeamDTO> getByOrganization(Organization organization) {
        return convertAll(getProvider().findByOrganization(organization));
    }


    public void checkNameExists(String teamName, String organizationName) throws TeamNotFoundException {
        final Organization organization = organizationProvider.findByName(organizationName).orElseThrow(() ->
                new OrganizationNotFoundException(this.getClass(), "Organization with name '" + organizationName + "' not found."));
        if (!getProvider().findByNameAndOrganization(teamName, organization).isPresent()) {
            throw new TeamNotFoundException(this.getClass(), "No team exists with name '" + teamName + "'.");
        }
    }


    public List<TeamDTO> getTeamsWithoutParent() {
        return getConverter().convertAll(getProvider().findByParentIsNull().stream().map(this::createConverterRequest).sorted().toList());
    }


    public List<TeamDTO> getTeamsWithParent() {
        return getConverter().convertAll(getProvider().findByParentIsNotNull().stream().map(this::createConverterRequest).toList());
    }


    public List<TeamDTO> getTeamsWithParent(Long parentId) {
        final Team team = getProvider().findById(parentId).orElseThrow(()
                -> new TeamNotFoundException(this.getClass(), "No Team exists with id '" + parentId + "'."));
        return getConverter().convertAll(getProvider().findByParent(team).stream().map(this::createConverterRequest).sorted().toList());
    }


    public List<TeamDTO> getTeamsWithParent(TeamDTO parent) {
        return getConverter().convertAll(getProvider().findByParent(reverse(parent)).stream().map(this::createConverterRequest).toList());
    }


    public int deleteByName(String name, String organizationName) {
        final Organization organization = organizationProvider.findByName(organizationName).orElseThrow(() ->
                new OrganizationNotFoundException(this.getClass(), "Organization with name '" + organizationName + "' not found."));
        return getProvider().deleteByName(name, organization);
    }

    private Team find(String organizationName, String name) {
        final Organization organization = organizationProvider.findByName(organizationName).orElseThrow(() ->
                new OrganizationNotFoundException(this.getClass(), "Organization with name '" + organizationName + "' not found."));
        return getProvider().findByNameAndOrganization(name, organization).orElseThrow(() ->
                new TeamNotFoundException(this.getClass(), "Team with name '" + name + "' not found."));
    }


    public TeamDTO assignByUserName(String organizationName, String name, Collection<String> usernames, String assignedBy) {
        return assign(organizationName, name, userConverter.convertAll(userProvider.findByUsernames(usernames).stream()
                .map(UserConverterRequest::new).toList()), assignedBy);
    }


    public TeamDTO assign(String organizationName, String name, Collection<UserDTO> users, String assignedBy) {
        final Team team = find(organizationName, name);
        return assign(team.getId(), users, assignedBy);
    }


    public TeamDTO assign(Long teamId, Collection<UserDTO> users, String assignedBy) {
        final Team team = getProvider().findById(teamId).orElseThrow(()
                -> new TeamNotFoundException(this.getClass(), "No Team exists with id '" + teamId + "'."));

        final List<Long> usersInTeam = userProvider.getByTeam(teamId).stream().map(User::getId).toList();

        users = users.stream().filter(userDTO -> !usersInTeam.contains(userDTO.getId())).toList();

        //Store into the team
        final List<TeamMember> teamMembers = new ArrayList<>();
        users.forEach(userDTO -> teamMembers.add(new TeamMember(teamId, userDTO.getId())));
        teamMemberProvider.saveAll(teamMembers);

        team.setUpdatedBy(assignedBy);

        return convert(getProvider().save(team));
    }

    public TeamDTO unAssignByUserName(String organizationName, String name, Collection<String> usernames, String assignedBy) {
        return unAssign(organizationName, name, userConverter.convertAll(userProvider.findByUsernames(usernames).stream()
                .map(UserConverterRequest::new).toList()), assignedBy);
    }


    public TeamDTO unAssign(String organizationName, String name, Collection<UserDTO> users, String assignedBy) {
        final Team team = find(organizationName, name);
        return unAssign(team.getId(), users, assignedBy);
    }

    public TeamDTO unAssign(Long teamId, Collection<UserDTO> users, String assignedBy) {
        final Team team = getProvider().findById(teamId).orElseThrow(()
                -> new TeamNotFoundException(this.getClass(), "No Team exists with id '" + teamId + "'."));


        final List<TeamMember> userGroupUserToDelete = new ArrayList<>();
        users.forEach(userDTO -> userGroupUserToDelete.add(new TeamMember(teamId, userDTO.getId())));
        teamMemberProvider.deleteAll(userGroupUserToDelete);

        team.setUpdatedBy(assignedBy);

        return convert(getProvider().save(team));
    }

    public List<TeamDTO> getFromUser(UUID userUUid) {
        return getFromUser(userProvider.findByUuid(userUUid).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No users found with uuid '" + userUUid + "'.")));
    }

    public List<TeamDTO> getFromUser(User user) {
        final Set<TeamMember> teamMembers = teamMemberProvider.findByIdUserId(user.getId());
        return get(teamMembers.stream().map(teamMember -> teamMember.getId().getTeamId()).sorted().toList());
    }


    @Transactional
    @Override
    public void delete(TeamDTO entity, String deletedBy) {
        DtoControllerLogger.info(this.getClass(), "Entity '{}' deleted by '{}'.", entity, deletedBy);
        teamMemberProvider.deleteByTeam(entity.getId());
        getProvider().delete(this.getConverter().reverse(entity));
    }


    @Transactional
    @Override
    public void deleteAll(String deletedBy) {
        DtoControllerLogger.info(this.getClass(), "All Entities deleted by '{}'.", deletedBy);
        final List<TeamDTO> teams = get();
        teams.forEach(team -> delete(team, deletedBy));
    }


    @Transactional
    @Override
    public void deleteById(Long id, String deletedBy) {
        DtoControllerLogger.warning(this.getClass(), "Deleting entity with id '{}' by '{}'.", id, deletedBy);
        final Optional<Team> team = getProvider().get(id);
        if (team.isEmpty()) {
            throw new TeamNotFoundException(this.getClass(), "No Teams exists with id '" + id + "'.");
        }
        delete(convert(team.get()), deletedBy);
    }
}
