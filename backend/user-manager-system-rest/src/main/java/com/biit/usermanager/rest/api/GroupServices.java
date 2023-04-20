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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupServices extends BasicServices<Group, GroupDTO, GroupRepository,
        GroupProvider, GroupConverterRequest, GroupConverter, GroupController> {

    public GroupServices(GroupController groupController) {
        super(groupController);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets an group by name.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/names/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GroupDTO get(@Parameter(description = "Name of an existing group", required = true) @PathVariable("name") String name,
                        HttpServletRequest request) {
        return controller.getByName(name);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Deletes an group by name.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/names/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@Parameter(description = "Name of an existing group", required = true) @PathVariable("name") String name,
                       HttpServletRequest request) {
        controller.deleteByName(name);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets all groups that does not have a parent.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/no-parent", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GroupDTO> getWithoutParent(HttpServletRequest request) {
        return controller.getGroupsWithoutParent();
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets all groups that has parent.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/has-parent", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GroupDTO> getWithParent(HttpServletRequest request) {
        return controller.getGroupsWithParent();
    }
}
