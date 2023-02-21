package com.biit.usermanager.core.converters;


import com.biit.server.controller.converters.ElementConverter;
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.persistence.entities.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserConverter extends ElementConverter<User, UserDTO, UserConverterRequest> {


    @Override
    protected UserDTO convertElement(UserConverterRequest from) {
        final UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(from.getEntity(), userDTO);
        return userDTO;
    }

    @Override
    public User reverse(UserDTO to) {
        if (to == null) {
            return null;
        }
        final User user = new User();
        BeanUtils.copyProperties(to, user);
        return user;
    }
}
