package com.biit.usermanager.rest.api;

import com.biit.server.rest.BasicServices;
import com.biit.usermanager.core.controller.GroupController;
import com.biit.usermanager.core.converters.GroupConverter;
import com.biit.usermanager.core.converters.models.GroupConverterRequest;
import com.biit.usermanager.core.providers.GroupProvider;
import com.biit.usermanager.dto.GroupDTO;
import com.biit.usermanager.persistence.entities.Group;
import com.biit.usermanager.persistence.repositories.GroupRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupServices extends BasicServices<Group, Long, GroupDTO, GroupRepository,
        GroupProvider, GroupConverterRequest, GroupConverter, GroupController> {

    public GroupServices(GroupController groupController) {
        super(groupController);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets a group by name.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/groups/{groupName}/applications/{applicationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GroupDTO get(@Parameter(description = "Name of an existing group", required = true)
                        @PathVariable("groupName") String groupName,
                        @Parameter(description = "Application name")
                        @PathVariable("applicationName") String applicationName,
                        HttpServletRequest request) {
        return getController().getByName(groupName, applicationName);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Deletes a group by name.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/groups/{groupName}/applications/{applicationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@Parameter(description = "Name of an existing group", required = true)
                       @PathVariable("groupName") String groupName,
                       @Parameter(description = "Application name")
                       @PathVariable("applicationName") String applicationName,
                       HttpServletRequest request) {
        getController().deleteByName(groupName, applicationName);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets all groups that does not have a parent.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/no-parent", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GroupDTO> getWithoutParent(HttpServletRequest request) {
        return getController().getGroupsWithoutParent();
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets all groups that has parent.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/has-parent", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GroupDTO> getWithParent(HttpServletRequest request) {
        return getController().getGroupsWithParent();
    }
}
