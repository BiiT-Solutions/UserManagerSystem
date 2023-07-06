package com.biit.usermanager.core.converters;


import com.biit.server.controller.converters.ElementConverter;
import com.biit.usermanager.core.converters.models.BasicUserConverterRequest;
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.dto.BasicUserDTO;
import com.biit.usermanager.persistence.entities.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class BasicUserConverter extends ElementConverter<User, BasicUserDTO, BasicUserConverterRequest> {


    @Override
    protected BasicUserDTO convertElement(BasicUserConverterRequest from) {
        final BasicUserDTO userDTO = new BasicUserDTO();
        BeanUtils.copyProperties(from.getEntity(), userDTO);
        if (from.getEntity().getLocale() != null) {
            userDTO.setLocale(Locale.forLanguageTag(from.getEntity().getLocale().replace("_", "-")));
        }
        return userDTO;
    }

    @Override
    public User reverse(BasicUserDTO to) {
        throw new UnsupportedOperationException("Users cannot be created from here.");
    }
}
