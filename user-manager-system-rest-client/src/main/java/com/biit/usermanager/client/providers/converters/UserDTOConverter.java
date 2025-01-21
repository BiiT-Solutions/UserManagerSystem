package com.biit.usermanager.client.providers.converters;

import com.biit.server.security.CreateUserRequest;
import com.biit.usermanager.dto.UserDTO;

public final class UserDTOConverter {

    private UserDTOConverter() {
    }

    public static UserDTO convert(CreateUserRequest createUserRequest) {
        final UserDTO userDTO = new UserDTO();
        userDTO.setFirstname(createUserRequest.getFirstname());
        userDTO.setLastName(createUserRequest.getLastname());
        userDTO.setUsername(createUserRequest.getUsername());
        userDTO.setExternalReference(createUserRequest.getExternalReference());
        if (createUserRequest.getEmail() != null && !createUserRequest.getEmail().isBlank()) {
            userDTO.setEmail(createUserRequest.getEmail());
        } else if (createUserRequest.getUsername().contains("@") && createUserRequest.getUsername().contains(".")) {
            userDTO.setEmail(createUserRequest.getUsername());
        }
        userDTO.setPassword(createUserRequest.getPassword());
        return userDTO;
    }
}
