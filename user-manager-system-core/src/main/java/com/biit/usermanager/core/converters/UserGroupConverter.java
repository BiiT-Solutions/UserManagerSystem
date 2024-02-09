package com.biit.usermanager.core.converters;


import com.biit.server.controller.converters.ElementConverter;
import com.biit.server.converters.ConverterUtils;
import com.biit.usermanager.core.converters.models.UserGroupConverterRequest;
import com.biit.usermanager.dto.UserGroupDTO;
import com.biit.usermanager.persistence.entities.UserGroup;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserGroupConverter extends ElementConverter<UserGroup, UserGroupDTO, UserGroupConverterRequest> {

    @Override
    protected UserGroupDTO convertElement(UserGroupConverterRequest from) {
        if (from.getEntity() == null) {
            return null;
        }
        final UserGroupDTO userGroupDTO = new UserGroupDTO();
        BeanUtils.copyProperties(from.getEntity(), userGroupDTO, ConverterUtils.getNullPropertyNames(from.getEntity()));
        return userGroupDTO;
    }

    @Override
    public UserGroup reverse(UserGroupDTO to) {
        if (to == null) {
            return null;
        }
        final UserGroup userGroup = new UserGroup();
        BeanUtils.copyProperties(to, userGroup, ConverterUtils.getNullPropertyNames(to));
        return userGroup;
    }
}
