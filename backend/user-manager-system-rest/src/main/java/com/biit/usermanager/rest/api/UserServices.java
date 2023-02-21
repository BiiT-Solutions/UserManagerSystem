package com.biit.usermanager.rest.api;

import com.biit.server.rest.BasicServices;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.converters.UserConverter;
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserServices extends BasicServices<User, UserDTO, UserRepository,
        UserProvider, UserConverterRequest, UserConverter, UserController> {

    public UserServices(UserController userController) {
        super(userController);
    }

    @PreAuthorize("hasRole('ROLE_VIEWER')")
    @Operation(summary = "Get user by name", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO get(@Parameter(description = "Name of an existing user", required = true) @PathVariable("name") String name,
                       HttpServletRequest request) {
        return controller.getByUserName(name);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get user by phone number", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/phone/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByPhone(@Parameter(description = "Phone of an existing user", required = true) @PathVariable("phone") String phone,
                              HttpServletRequest request) {
        return controller.getByPhone(phone);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Gets a list of expired users", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/account_expired/{account_expired}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getByAccountExpired(@Parameter(description = "Account is expired", required = true)
                                             @PathVariable("account_expired") boolean accountExpired,
                                             HttpServletRequest request) {
        return controller.getAllByExpired(accountExpired);
    }

    @PreAuthorize("hasRole('ROLE_USER_MANAGER_ADMIN')")
    @Operation(summary = "Gets all enable/disable users .", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/enable/{enable}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getEnabled(@Parameter(description = "enable/disable", required = true)
                                    @PathVariable("enable") boolean enable, HttpServletRequest request) {
        return controller.getByEnable(enable);
    }
}
