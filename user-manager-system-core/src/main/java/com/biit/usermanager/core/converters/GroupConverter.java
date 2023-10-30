package com.biit.usermanager.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.server.converters.ConverterUtils;
import com.biit.usermanager.core.converters.models.ApplicationConverterRequest;
import com.biit.usermanager.core.converters.models.GroupConverterRequest;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.dto.GroupDTO;
import com.biit.usermanager.persistence.entities.Group;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.stereotype.Component;

@Component
public class GroupConverter extends ElementConverter<Group, GroupDTO, GroupConverterRequest> {

    private final ApplicationConverter applicationConverter;
    private final ApplicationProvider applicationProvider;

    public GroupConverter(ApplicationConverter applicationConverter, ApplicationProvider applicationProvider) {
        this.applicationConverter = applicationConverter;
        this.applicationProvider = applicationProvider;
    }

    @Override
    protected GroupDTO convertElement(GroupConverterRequest from) {
        final GroupDTO groupDTO = new GroupDTO();
        BeanUtils.copyProperties(from.getEntity(), groupDTO, ConverterUtils.getNullPropertyNames(from.getEntity()));
        if (from.getEntity().getParent() != null) {
            groupDTO.setParent(convertElement(new GroupConverterRequest(from.getEntity().getParent())));
        }

        try {
            //Converter can have the tournament defined already.
            if (from.getApplication() != null) {
                groupDTO.setApplication(applicationConverter.convert(
                        new ApplicationConverterRequest(from.getApplication())));
            } else {
                groupDTO.setApplication(applicationConverter.convert(
                        new ApplicationConverterRequest(from.getEntity().getApplication())));
            }
        } catch (LazyInitializationException | FatalBeanException e) {
            groupDTO.setApplication(applicationConverter.convert(
                    new ApplicationConverterRequest(applicationProvider.get(from.getEntity().getApplication().getId()).orElse(null))));
        }
        return groupDTO;
    }

    @Override
    public Group reverse(GroupDTO to) {
        if (to == null) {
            return null;
        }
        final Group group = new Group();
        BeanUtils.copyProperties(to, group, ConverterUtils.getNullPropertyNames(to));
        if (to.getParent() != null) {
            group.setParent(reverse((to.getParent())));
        }
        if (to.getApplication() != null) {
            group.setApplication(applicationConverter.reverse(to.getApplication()));
        }
        return group;
    }
}
