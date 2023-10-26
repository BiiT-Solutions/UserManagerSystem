package com.biit.usermanager.core.converters;


import com.biit.server.controller.converters.ElementConverter;
import com.biit.usermanager.core.converters.models.ApplicationConverterRequest;
import com.biit.usermanager.core.converters.models.ApplicationRoleConverterRequest;
import com.biit.usermanager.core.converters.models.RoleConverterRequest;
import com.biit.usermanager.dto.ApplicationRoleDTO;
import com.biit.usermanager.dto.ApplicationRoleIdDTO;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.ApplicationRoleId;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRoleConverter extends ElementConverter<ApplicationRole, ApplicationRoleDTO, ApplicationRoleConverterRequest> {
    private final RoleConverter roleConverter;

    private final ApplicationConverter applicationConverter;


    public ApplicationRoleConverter(RoleConverter roleConverter, ApplicationConverter applicationConverter) {
        this.roleConverter = roleConverter;
        this.applicationConverter = applicationConverter;
    }


    @Override
    protected ApplicationRoleDTO convertElement(ApplicationRoleConverterRequest from) {
        final ApplicationRoleDTO applicationRoleDTO = new ApplicationRoleDTO();
        BeanUtils.copyProperties(from.getEntity(), applicationRoleDTO);
        applicationRoleDTO.setId(new ApplicationRoleIdDTO(
                        applicationConverter.convert(new ApplicationConverterRequest(from.getEntity().getId().getApplication())),
                        roleConverter.convert(new RoleConverterRequest(from.getEntity().getId().getRole()))
                )
        );
        return applicationRoleDTO;
    }

    @Override
    public ApplicationRole reverse(ApplicationRoleDTO to) {
        if (to == null) {
            return null;
        }
        final ApplicationRole applicationRole = new ApplicationRole();
        BeanUtils.copyProperties(to, applicationRole);
        applicationRole.setId(new ApplicationRoleId(
                applicationConverter.reverse(to.getId().getApplication()),
                roleConverter.reverse(to.getId().getRole())
        ));
        return applicationRole;
    }
}
