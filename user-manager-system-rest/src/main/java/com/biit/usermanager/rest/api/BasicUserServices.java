package com.biit.usermanager.rest.api;

import com.biit.server.rest.ElementServices;
import com.biit.usermanager.core.controller.BasicUserController;
import com.biit.usermanager.core.converters.BasicUserConverter;
import com.biit.usermanager.core.converters.models.BasicUserConverterRequest;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.dto.BasicUserDTO;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users/basic")
public class BasicUserServices extends ElementServices<User, Long, BasicUserDTO, UserRepository,
        UserProvider, BasicUserConverterRequest, BasicUserConverter, BasicUserController> {

    public BasicUserServices(BasicUserController userController) {
        super(userController);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get user by username", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/usernames/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BasicUserDTO getByUsername(@Parameter(description = "Username of an existing user", required = true) @PathVariable("username") String username,
                                      HttpServletRequest request) {
        return getController().getByUsername(username);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get user by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/uuids/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BasicUserDTO getByUUID(@Parameter(description = "Name of an existing user", required = true) @PathVariable("uuid") String uuid,
                                  HttpServletRequest request) {
        return (BasicUserDTO) getController().findByUID(uuid).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                "No User with uuid '" + uuid + "' found on the system."));
    }

    @Operation(hidden = true)
    @Override
    public BasicUserDTO add(@RequestBody BasicUserDTO dto, Authentication authentication, HttpServletRequest request) {
        throw new UnsupportedOperationException("Method not valid!");
    }

    @Operation(hidden = true)
    @Override
    public List<BasicUserDTO> add(@RequestBody Collection<BasicUserDTO> dtos, Authentication authentication, HttpServletRequest request) {
        throw new UnsupportedOperationException("Method not valid!");
    }

    @Operation(hidden = true)
    @Override
    public BasicUserDTO update(@RequestBody BasicUserDTO dto, Authentication authentication, HttpServletRequest request) {
        throw new UnsupportedOperationException("Method not valid!");
    }

    @Operation(hidden = true)
    @Override
    public void delete(@RequestBody BasicUserDTO dto, Authentication authentication, HttpServletRequest request) {
        throw new UnsupportedOperationException("Method not valid!");
    }

    @Operation(hidden = true)
    @Override
    public void delete(Long id, HttpServletRequest request) {
        throw new UnsupportedOperationException("Method not valid!");
    }
}
