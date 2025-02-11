package com.biit.usermanager.rest.api;

import com.biit.usermanager.core.controller.TemporalTokenController;
import com.biit.usermanager.dto.TemporalTokenDTO;
import com.biit.usermanager.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/temporal-tokens")
public class TemporalTokenServices {

    private final TemporalTokenController temporalTokenController;

    public TemporalTokenServices(TemporalTokenController temporalTokenController) {
        this.temporalTokenController = temporalTokenController;
    }


    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Creates a temporal token for a user.", description = "This token can be used for granting access to an application.")
    @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TemporalTokenDTO getTemporalToken(
            @Parameter(description = "UUID from an existing user", required = true) @PathVariable("uuid") UUID uuid, HttpServletRequest request) {
        return temporalTokenController.generateTemporalToken(uuid);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Creates a temporal token for a user.", description = "This token can be used for granting access to an application.")
    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public TemporalTokenDTO getTemporalToken(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        return temporalTokenController.generateTemporalToken(userDTO);
    }
}
