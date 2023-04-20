package com.biit.usermanager.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.usermanager.core.converters.models.GroupConverterRequest;
import com.biit.usermanager.dto.GroupDTO;
import com.biit.usermanager.persistence.entities.Group;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class GroupConverter extends ElementConverter<Group, GroupDTO, GroupConverterRequest> {


    @Override
    protected GroupDTO convertElement(GroupConverterRequest from) {
        final GroupDTO groupDTO = new GroupDTO();
        BeanUtils.copyProperties(from.getEntity(), groupDTO);
        return groupDTO;
    }

    @Override
    public Group reverse(GroupDTO to) {
        if (to == null) {
            return null;
        }
        final Group group = new Group();
        BeanUtils.copyProperties(to, group);
        return group;
    }
}
