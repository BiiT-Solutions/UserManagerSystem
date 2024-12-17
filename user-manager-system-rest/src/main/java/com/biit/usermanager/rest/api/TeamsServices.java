package com.biit.usermanager.rest.api;

import com.biit.server.rest.ElementServices;
import com.biit.server.security.rest.NetworkController;
import com.biit.usermanager.core.controller.TeamController;
import com.biit.usermanager.core.converters.TeamConverter;
import com.biit.usermanager.core.converters.models.TeamConverterRequest;
import com.biit.usermanager.core.providers.TeamProvider;
import com.biit.usermanager.dto.TeamDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.persistence.entities.Team;
import com.biit.usermanager.persistence.repositories.TeamRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teams")
public class TeamsServices extends ElementServices<Team, Long, TeamDTO, TeamRepository,
        TeamProvider, TeamConverterRequest, TeamConverter, TeamController> {

    private final NetworkController networkController;

    public TeamsServices(TeamController teamController, NetworkController networkController) {
        super(teamController);
        this.networkController = networkController;
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets a team by name.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{teamName}/organizations/{organizationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TeamDTO get(@Parameter(description = "Name of an existing team", required = true)
                       @PathVariable("teamName") String teamName,
                       @Parameter(description = "Organization name")
                       @PathVariable("organizationName") String organizationName,
                       HttpServletRequest request) {
        return getController().getByName(teamName, organizationName);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets all teams from an organization.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/organizations/{organizationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TeamDTO> get(@Parameter(description = "Organization name")
                             @PathVariable("organizationName") String organizationName,
                             HttpServletRequest request) {
        return getController().getByOrganization(organizationName);
    }


    @Operation(summary = "Gets all teams from an organization. This method is public!")
    @GetMapping(value = "/public/organizations/{organizationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getNames(@Parameter(description = "Organization name")
                                 @PathVariable("organizationName") String organizationName,
                                 HttpServletRequest request) {
        UserManagerLogger.warning(this.getClass(), "Requesting teams from organization '" + organizationName + "' on ip '"
                + networkController.getClientIP(request) + "'.");
        return getController().getByOrganization(organizationName).stream().filter(Objects::nonNull).map(TeamDTO::getName).collect(Collectors.toList());
    }


    @Operation(summary = "Checks if a teamName is already taken or not.")
    @GetMapping(path = "/{teamName}/organizations/{organizationName}/check")
    @ResponseStatus(value = HttpStatus.OK)
    public void checkTeamNameExists(@Parameter(description = "Name of an existing team", required = true)
                                    @PathVariable("teamName") String teamName,
                                    @Parameter(description = "Organization name")
                                    @PathVariable("organizationName") String organizationName,
                                    HttpServletRequest httpRequest) {
        getController().checkNameExists(teamName, organizationName);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Deletes a team by name.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{teamName}/organizations/{organizationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@Parameter(description = "Name of an existing team", required = true)
                       @PathVariable("teamName") String teamName,
                       @Parameter(description = "Organization name")
                       @PathVariable("organizationName") String organizationName,
                       HttpServletRequest request) {
        getController().deleteByName(teamName, organizationName);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets all teams that does not have a parent.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/no-parent", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TeamDTO> getWithoutParent(HttpServletRequest request) {
        return getController().getTeamsWithoutParent();
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets all teams that has parent.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/has-parent", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TeamDTO> getWithParent(HttpServletRequest request) {
        return getController().getTeamsWithParent();
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets all teams that has parent.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/parent/{parentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TeamDTO> getWithParent(@Parameter(description = "Id of a parent Team", required = true)
                                       @PathVariable("parentId") Long parentId,
                                       HttpServletRequest request) {
        return getController().getTeamsWithParent(parentId);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Adds members to a team.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/{id}/users",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TeamDTO addUsers(
            @Parameter(description = "Id of an existing Team", required = true)
            @PathVariable("id") Long id,
            @RequestBody Collection<UserDTO> users,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().assign(id, users, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Removes members from a Team.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/{id}/users/remove",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TeamDTO removeUsers(
            @Parameter(description = "Id of an existing Team", required = true)
            @PathVariable("id") Long id,
            @RequestBody Collection<UserDTO> users,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().unAssign(id, users, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Gets all teams from a user.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/users/{userUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<TeamDTO> getTeams(
            @Parameter(description = "UUid of an existing User", required = true)
            @PathVariable("userUuid") UUID uuid, Authentication authentication, HttpServletRequest request) {
        return getController().getFromUser(uuid);
    }
}
