package com.biit.usermanager.core.converters;


import com.biit.server.controller.converters.ElementConverter;
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.persistence.entities.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class UserConverter extends ElementConverter<User, UserDTO, UserConverterRequest> {


    @Override
    protected UserDTO convertElement(UserConverterRequest from) {
        final UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(from.getEntity(), userDTO);
        if (from.getEntity().getLocale() != null) {
            userDTO.setLocale(Locale.forLanguageTag(from.getEntity().getLocale().replace("_", "-")));
        }
        return userDTO;
    }

    @Override
    public User reverse(UserDTO to) {
        if (to == null) {
            return null;
        }
        final User user = new User();
        BeanUtils.copyProperties(to, user);
        if (to.getLocale() != null) {
            user.setLocale(to.getLocale().toLanguageTag().replace("-", "_"));
        }
        return user;
    }
}
