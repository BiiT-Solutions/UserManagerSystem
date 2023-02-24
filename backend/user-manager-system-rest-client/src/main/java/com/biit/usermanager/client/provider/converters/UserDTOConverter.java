package com.biit.usermanager.client.provider.converters;

import com.biit.server.security.CreateUserRequest;
import com.biit.usermanager.dto.UserDTO;

public class UserDTOConverter {

    private UserDTOConverter() {
    }

    public static UserDTO convert(CreateUserRequest createUserRequest) {
        final UserDTO userDTO = new UserDTO();
        userDTO.setName(createUserRequest.getName());
        userDTO.setLastName(createUserRequest.getLastname());
        userDTO.setUsername(createUserRequest.getUsername());
        userDTO.setPassword(createUserRequest.getPassword());
        return userDTO;
    }
}
