package com.biit.usermanager.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.usermanager.core.converters.models.RoleConverterRequest;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.persistence.entities.Role;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter extends ElementConverter<Role, RoleDTO, RoleConverterRequest> {


    @Override
    protected RoleDTO convertElement(RoleConverterRequest from) {
        final RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(from.getEntity(), roleDTO);
        return roleDTO;
    }

    @Override
    public Role reverse(RoleDTO to) {
        if (to == null) {
            return null;
        }
        final Role role = new Role();
        BeanUtils.copyProperties(to, role);
        return role;
    }
}
